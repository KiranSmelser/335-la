package musicstore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Loads and provides access to a "database" of albums and songs
 */
public class MusicStore {
	private final Map<String, Album> albumsByTitle;
    private final Map<String, List<Album>> albumsByArtist;
    private final Map<String, List<Song>> songsByTitle;
    private final Map<String, List<Song>> songsByArtist;

    /**
     * Constructs a MusicStore object and loads the album data from files
     *
     * @pre albumsTxtPath != null && albumsDirectoryPath != null
     * @throws IOException if files are missing or unreadable
     */
    public MusicStore(String albumsTxtPath, String albumsDirectoryPath) throws IOException {
        this.albumsByTitle = new HashMap<>();
        this.albumsByArtist = new HashMap<>();
        this.songsByTitle = new HashMap<>();
        this.songsByArtist = new HashMap<>();

        loadAllAlbums(albumsTxtPath, albumsDirectoryPath);
    }

    /**
     * Reads "albums.txt" line-by-line, builds each album's filename,
     * and loads that album into the store
     */
    private void loadAllAlbums(String albumsTxtPath, String albumsDirectoryPath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(albumsTxtPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }
                String albumTitle = parts[0].trim();
                String artist = parts[1].trim();

                // Build the album file name
                String filename = albumTitle + "_" + artist + ".txt";
                String fullPath = albumsDirectoryPath + "/" + filename;

                readAlbumFile(fullPath);
            }
        }
    }

    /**
     * Reads a single album file
     */
    private void readAlbumFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // First line
            String heading = br.readLine();
            if (heading == null) {
                return;	// probably should throw an exception here
            }

            String[] parts = heading.split(",");
            if (parts.length < 4) {
                return; // probably should throw an exception here
            }
            String albumTitle = parts[0].trim();
            String artist = parts[1].trim();
            String genre = parts[2].trim();
            int year = Integer.parseInt(parts[3].trim());

            // Read remaining lines
            ArrayList<Song> songs = new ArrayList<>();
            String songLine;
            while ((songLine = br.readLine()) != null) {
                String songTitle = songLine.trim();
                if (!songTitle.isEmpty()) {
                    Song song = new Song(songTitle, artist, albumTitle);
                    songs.add(song);

                    // Store in songsByTitle
                    String lowerSongTitle = songTitle.toLowerCase();
                    songsByTitle.computeIfAbsent(lowerSongTitle, k -> new ArrayList<>()).add(song);

                    // Store in songsByArtist
                    String lowerArtist = artist.toLowerCase();
                    songsByArtist.computeIfAbsent(lowerArtist, k -> new ArrayList<>()).add(song);
                }
            }

            // Create the album object
            Album album = new Album(albumTitle, artist, genre, year, songs);

            // Store in albumsByTitle
            albumsByTitle.put(albumTitle.toLowerCase(), album);

            // Store in albumsByArtist
            String lowerArtist = artist.toLowerCase();
            albumsByArtist.computeIfAbsent(lowerArtist, k -> new ArrayList<>()).add(album);
        }
    }

    /**
     * Retrieves a single Album by title
     * 
     * @pre albumTitle != null
     */
    public Album getAlbumByTitle(String albumTitle) {
        return albumsByTitle.get(albumTitle.toLowerCase());
    }

    /**
     * Retrieves all Albums by artist name
     * 
     * @pre artist != null
     */
    public List<Album> getAlbumsByArtist(String artist) {
        return Collections.unmodifiableList(albumsByArtist.getOrDefault(artist.toLowerCase(), Collections.emptyList()));
    }

    /**
     * Retrieves all Songs that match a title
     * 
     * @pre songTitle != null
     */
    public List<Song> getSongsByTitle(String songTitle) {
        return Collections.unmodifiableList(songsByTitle.getOrDefault(songTitle.toLowerCase(), Collections.emptyList()));
    }

    /**
     * Retrieves all Songs by an artist
     * 
     * @pre artist != null
     */
    public List<Song> getSongsByArtist(String artist) {
        return Collections.unmodifiableList(songsByArtist.getOrDefault(artist.toLowerCase(), Collections.emptyList()));
    }
}
