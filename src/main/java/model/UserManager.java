package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages user creation, login, and loading/saving library data
 */
public class UserManager {

    private final Map<String, User> usersByUsername;
    private final File userFile;
    private final MusicStore store;
    private User currentUser;

    /**
     * Constructs a UserManager object and loads existing users
     *
     * @pre userJsonPath != null && store != null
     * @throws IOException if files are missing or unreadable
     */
    public UserManager(String userJsonPath, MusicStore store) throws IOException {
        this.userFile = new File(userJsonPath);
        this.store = store;
        this.usersByUsername = new HashMap<>();
        this.currentUser = null;

        loadUsersFromJson();
    }

    /**
     * Creates new user if not already present
     */
    public boolean createUser(String username, String plainPassword) throws IOException {
        if (usersByUsername.containsKey(username.toLowerCase())) {
            return false;
        }
        String salt = generateSalt();
        String hash = hashPassword(plainPassword, salt);
        User newUser = new User(username, salt, hash);
        usersByUsername.put(username.toLowerCase(), newUser);
        saveUsersToJson();
        return true;
    }

    /**
     * Attempts to authenticate user
     */
    public boolean authenticate(String username, String plainPassword) {
        User u = usersByUsername.get(username.toLowerCase());
        if (u == null) return false;  // user doesn't exist

        String testHash = hashPassword(plainPassword, u.getSalt());
        if (testHash.equals(u.getPasswordHash())) {
            this.currentUser = u;
            return true;
        }
        return false;
    }

    /**
     * Returns currently logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logs out current user
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Loads current user's library data from JSON
     */
    public void loadUserLibrary(LibraryModel library) throws IOException {
        if (currentUser == null) {
            return;
        }
        File libraryFile = new File("library_" + currentUser.getUsername() + ".json");
        if (!libraryFile.exists()) {
            // no library data yet so user is new
            return;
        }
        String content = new String(Files.readAllBytes(libraryFile.toPath()), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        // Load songs
        JSONArray songsArr = json.optJSONArray("songs");
        if (songsArr != null) {
            for (int i = 0; i < songsArr.length(); i++) {
                JSONObject sObj = songsArr.getJSONObject(i);
                String title = sObj.getString("title");
                String artist = sObj.getString("artist");
                String albumTitle = sObj.getString("albumTitle");

                Song storeSong = findSongInStore(title, artist, albumTitle);
                if (storeSong != null) {
                    library.addSong(storeSong);
                }
            }
        }

        // Load albums
        JSONArray albumsArr = json.optJSONArray("albums");
        if (albumsArr != null) {
            for (int i = 0; i < albumsArr.length(); i++) {
                JSONObject aObj = albumsArr.getJSONObject(i);
                String albumTitle = aObj.getString("title");
                String artist = aObj.getString("artist");

                Album storeAlbum = findAlbumInStore(albumTitle, artist);
                if (storeAlbum != null) {
                    library.addAlbum(storeAlbum);
                }
            }
        }

        // Load rated songs if they're present in library
        JSONArray ratedArr = json.optJSONArray("ratedSongs");
        if (ratedArr != null) {
            for (int i = 0; i < ratedArr.length(); i++) {
                JSONObject rObj = ratedArr.getJSONObject(i);
                String title = rObj.getString("title");
                String artist = rObj.getString("artist");
                int rating = rObj.getInt("rating");

                List<Song> matches = library.getSongsByTitle(title);
                for (Song s : matches) {
                    if (s.getArtist().equalsIgnoreCase(artist)) {
                        library.rateSong(s, rating);
                    }
                }
            }
        }

        // Load playlists
        JSONArray playlistsArr = json.optJSONArray("playlists");
        if (playlistsArr != null) {
            for (int i = 0; i < playlistsArr.length(); i++) {
                JSONObject pObj = playlistsArr.getJSONObject(i);
                String playlistName = pObj.getString("name");
                library.createPlayList(playlistName);

                JSONArray plSongs = pObj.optJSONArray("songs");
                if (plSongs != null) {
                    for (int j = 0; j < plSongs.length(); j++) {
                        JSONObject sObj = plSongs.getJSONObject(j);
                        String t = sObj.getString("title");
                        String art = sObj.getString("artist");

                        List<Song> matches = library.getSongsByTitle(t);
                        for (Song s : matches) {
                            if (s.getArtist().equalsIgnoreCase(art)) {
                                library.getPlayList(playlistName).addSong(s);
                            }
                        }
                    }
                }
            }
        }
        
     // 5) Load 'recentlyPlayedSongs'
        JSONArray recentArr = json.optJSONArray("recentlyPlayedSongs");
        if (recentArr != null) {
            for (int i = 0; i < recentArr.length(); i++) {
                JSONObject sObj = recentArr.getJSONObject(i);
                String title = sObj.getString("title");
                String artist = sObj.getString("artist");
                String albumTitle = sObj.getString("albumTitle");

                List<Song> matches = library.getSongsByTitle(title);
                for (Song s : matches) {
                    if (s.getArtist().equalsIgnoreCase(artist)
                            && s.getAlbumTitle().equalsIgnoreCase(albumTitle)) {
                        library.getRecentlyPlayedPlayList().addSongRecent(s);
                        break;
                    }
                }
            }
        }

        // 6) Load 'frequentlyPlayedSongs'
        JSONArray freqArr = json.optJSONArray("frequentlyPlayedSongs");
        if (freqArr != null) {
            for (int i = 0; i < freqArr.length(); i++) {
                JSONObject sObj = freqArr.getJSONObject(i);
                String title = sObj.getString("title");
                String artist = sObj.getString("artist");
                String albumTitle = sObj.getString("albumTitle");

                List<Song> matches = library.getSongsByTitle(title);
                for (Song s : matches) {
                    if (s.getArtist().equalsIgnoreCase(artist)
                            && s.getAlbumTitle().equalsIgnoreCase(albumTitle)) {
                        library.getFrequentlyPlayedPlayList().addSongFrequent(s);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Saves the current user's library data
     */
    public void saveUserLibrary(LibraryModel library) throws IOException {
        if (currentUser == null) {
            return;
        }
        File libraryFile = new File("library_" + currentUser.getUsername() + ".json");
        JSONObject root = new JSONObject();

        // Save songs
        JSONArray songsArr = new JSONArray();
        for (Song s : library.getSongs()) {
            JSONObject sObj = new JSONObject();
            sObj.put("title", s.getTitle());
            sObj.put("artist", s.getArtist());
            sObj.put("albumTitle", s.getAlbumTitle());
            songsArr.put(sObj);
        }
        root.put("songs", songsArr);

        // Save albums
        JSONArray albumsArr = new JSONArray();
        for (Album a : library.getAlbums()) {
            JSONObject aObj = new JSONObject();
            aObj.put("title", a.getTitle());
            aObj.put("artist", a.getArtist());
            aObj.put("genre", a.getGenre());
            aObj.put("year", a.getYear());

            // Save album's songs
            JSONArray aSongs = new JSONArray();
            for (Song s : a.getSongs()) {
                JSONObject sObj = new JSONObject();
                sObj.put("title", s.getTitle());
                sObj.put("artist", s.getArtist());
                sObj.put("albumTitle", s.getAlbumTitle());
                aSongs.put(sObj);
            }
            aObj.put("songs", aSongs);

            albumsArr.put(aObj);
        }
        root.put("albums", albumsArr);

        // Save rated songs
        JSONArray ratedArr = new JSONArray();
        Map<Song, Integer> ratedMap = library.getAllRatedSongs();
        for (Map.Entry<Song, Integer> entry : ratedMap.entrySet()) {
            Song s = entry.getKey();
            int r = entry.getValue();
            JSONObject rObj = new JSONObject();
            rObj.put("title", s.getTitle());
            rObj.put("artist", s.getArtist());
            rObj.put("rating", r);
            ratedArr.put(rObj);
        }
        root.put("ratedSongs", ratedArr);

        // Save playlists
        JSONArray playlistsArr = new JSONArray();
        for (PlayList pl : library.getPlayLists()) {
            JSONObject plObj = new JSONObject();
            plObj.put("name", pl.getName());
            JSONArray plSongsArr = new JSONArray();
            for (Song s : pl.getSongs()) {
                JSONObject sObj = new JSONObject();
                sObj.put("title", s.getTitle());
                sObj.put("artist", s.getArtist());
                plSongsArr.put(sObj);
            }
            plObj.put("songs", plSongsArr);
            playlistsArr.put(plObj);
        }
        root.put("playlists", playlistsArr);
        
     // 5) Save recentlyPlayedSongs
        JSONArray recentArr = new JSONArray();
        for (Song s : library.getRecentlyPlayedSongs()) {
            JSONObject sObj = new JSONObject();
            sObj.put("title", s.getTitle());
            sObj.put("artist", s.getArtist());
            sObj.put("albumTitle", s.getAlbumTitle());
            recentArr.put(sObj);
        }
        root.put("recentlyPlayedSongs", recentArr);

        // 6) Save frequentlyPlayedSongs
        JSONArray freqArr = new JSONArray();
        for (Song s : library.getFrequentlyPlayedSongs()) {
            JSONObject sObj = new JSONObject();
            sObj.put("title", s.getTitle());
            sObj.put("artist", s.getArtist());
            sObj.put("albumTitle", s.getAlbumTitle());
            freqArr.put(sObj);
        }
        root.put("frequentlyPlayedSongs", freqArr);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(libraryFile))) {
            bw.write(root.toString(2));
        }
    }

    /**
     * Helper method for loading user credentials from file
     */
    private void loadUsersFromJson() throws IOException {
        if (!userFile.exists()) {
            // no existing users
            return;
        }
        String content = new String(Files.readAllBytes(userFile.toPath()), StandardCharsets.UTF_8);
        JSONArray arr = new JSONArray(content);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String username = obj.getString("username");
            String salt = obj.getString("salt");
            String passHash = obj.getString("passwordHash");
            User u = new User(username, salt, passHash);
            usersByUsername.put(username.toLowerCase(), u);
        }
    }

    /**
     * Helper method for saving user credentials in JSON format
     */
    private void saveUsersToJson() throws IOException {
        JSONArray arr = new JSONArray();
        for (User u : usersByUsername.values()) {
            JSONObject obj = new JSONObject();
            obj.put("username", u.getUsername());
            obj.put("salt", u.getSalt());
            obj.put("passwordHash", u.getPasswordHash());
            arr.put(obj);
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
            bw.write(arr.toString(2));
        }
    }

    /**
     * Helper method for generating a 16-byte random salt
     */
    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return bytesToHex(salt);
    }

    /**
     * Helper method for hashing password + salt
     */
    private String hashPassword(String plainPassword, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String salted = plainPassword + salt;
            byte[] hashedBytes = md.digest(salted.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Helper method for searching the store for a Song
     */
    private Song findSongInStore(String title, String artist, String albumTitle) {
        if (store == null) return null;

        List<Song> byTitle = store.getSongsByTitle(title);
        for (Song s : byTitle) {
            if (s.getArtist().equalsIgnoreCase(artist)
                    && s.getAlbumTitle().equalsIgnoreCase(albumTitle)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Helper method for searching the store for an Album
     */
    private Album findAlbumInStore(String albumTitle, String artist) {
        if (store == null) return null;

        Album found = store.getAlbumByTitle(albumTitle);
        if (found != null && found.getArtist().equalsIgnoreCase(artist)) {
            return found;
        }
        return null;
    }
}