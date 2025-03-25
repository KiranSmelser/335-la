package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * JUnit tests for UserManager class
 */
class UserManagerTest {

    @TempDir
    Path tempDir; // temp directory for each test class

    private File userJsonFile;
    private UserManager userManager;
    private LibraryModel library;
    private MusicStore musicStore;

    @BeforeEach
    void setUp() throws IOException {
        userJsonFile = new File(tempDir.toFile(), "users.json");

        String albumsTxtPath = "src/main/resources/albums.txt";
        String albumsDirPath = "src/main/resources/albums";
        musicStore = new MusicStore(albumsTxtPath, albumsDirPath);

        userManager = new UserManager(userJsonFile.getAbsolutePath(), musicStore);

        library = new LibraryModel();
    }

    /**
     * Clean up JSON files after each test
     */
    @AfterEach
    void tearDown() {
        File[] tempFiles = tempDir.toFile().listFiles();
        if (tempFiles != null) {
            for (File f : tempFiles) {
                f.delete();
            }
        }

        File cwd = new File(".");
        File[] libraryFiles = cwd.listFiles((dir, name) -> name.startsWith("library_") && name.endsWith(".json"));
        if (libraryFiles != null) {
            for (File lf : libraryFiles) {
                lf.delete();
            }
        }
    }

    @Test
    void testCreateUserSuccess() throws IOException {
        boolean created = userManager.createUser("alice", "password123");
        assertTrue(created);

        assertTrue(userManager.authenticate("alice", "password123"));
        assertNotNull(userManager.getCurrentUser());
        assertEquals("alice", userManager.getCurrentUser().getUsername());
    }

    @Test
    void testCreateUserDuplicate() throws IOException {
        boolean first = userManager.createUser("bob", "random123");
        assertTrue(first);

        boolean second = userManager.createUser("bob", "random");
        assertFalse(second);
    }

    @Test
    void testAuthenticateWrongPassword() throws IOException {
        userManager.createUser("charlie", "correctPassword");
        assertFalse(userManager.authenticate("charlie", "incorrectPassword"));
        assertNull(userManager.getCurrentUser());
    }

    @Test
    void testAuthenticateNonexistentUser() {
        assertFalse(userManager.authenticate("DNE", "123"));
        assertNull(userManager.getCurrentUser());
    }

    @Test
    void testLogout() throws IOException {
        userManager.createUser("david", "Password");
        assertTrue(userManager.authenticate("david", "Password"));
        assertNotNull(userManager.getCurrentUser());

        userManager.logout();
        assertNull(userManager.getCurrentUser());
    }

    @Test
    void testLoadUserLibrary_NoExistingFile() throws IOException {
        userManager.createUser("erin", "123");
        assertTrue(userManager.authenticate("erin", "123"));

        userManager.loadUserLibrary(library);
        assertTrue(library.getSongs().isEmpty());
        assertTrue(library.getAlbums().isEmpty());
    }

    @Test
    void testLoadAndSaveUserLibrary_NonStoreAlbumIsSkipped() throws IOException {
        userManager.createUser("tmp", "pass");
        assertTrue(userManager.authenticate("tmp", "pass"));

        Song fakeSong = new Song("Purple Rain", "Prince", "Purple Rain");
        Album fakeAlbum = new Album("Purple Rain", "Prince", "Rock", 1984, List.of(fakeSong));

        library.addAlbum(fakeAlbum);
        userManager.saveUserLibrary(library);
        LibraryModel loaded = new LibraryModel();
        userManager.loadUserLibrary(loaded);

        assertTrue(loaded.getAlbums().isEmpty());
        assertTrue(loaded.getSongs().isEmpty());
    }

    @Test
    void testLoadAndSaveUserLibrary_AlbumsInStore() throws IOException {
        userManager.createUser("User", "pass123");
        assertTrue(userManager.authenticate("User", "pass123"));

        Album storeAlbum = musicStore.getAlbumByTitle("21");
        assertNotNull(storeAlbum);

        library.addAlbum(storeAlbum);
        userManager.saveUserLibrary(library);
        LibraryModel loaded = new LibraryModel();
        userManager.loadUserLibrary(loaded);

        List<Album> loadedAlbums = loaded.getAlbums();
        assertEquals(1, loadedAlbums.size());
        
        Album la = loadedAlbums.get(0);
        assertEquals(storeAlbum.getTitle(), la.getTitle());
        assertEquals(storeAlbum.getArtist(), la.getArtist());

        assertFalse(loaded.getSongs().isEmpty());
        assertTrue(loaded.getSongsByTitle("Rolling in the Deep").size() >= 1);
    }

    @Test
    void testSaveLibrary_NoCurrentUser() throws IOException {
        userManager.logout();
        library.addSong(new Song("TestSong", "TestArtist", "TestAlbum"));
        userManager.saveUserLibrary(library);
        assertTrue(true);
    }

    @Test
    void testReloadExistingUserFile() throws IOException {
        userManager.createUser("harry", "secretPassword");
        userManager.logout();


        String existingPath = userJsonFile.getAbsolutePath();
        userManager = new UserManager(existingPath, musicStore);

        assertTrue(userManager.authenticate("harry", "secretPassword"));
        assertNotNull(userManager.getCurrentUser());
        assertEquals("harry", userManager.getCurrentUser().getUsername());
    }

    @Test
    void testLoadAndSaveUserLibrary_RatedSongsAndPlaylists() throws IOException {
        userManager.createUser("testUser", "pass123");
        assertTrue(userManager.authenticate("testUser", "pass123"));

        Album storeAlbum = musicStore.getAlbumByTitle("21");
        assertNotNull(storeAlbum);

        library.addAlbum(storeAlbum);
        List<Song> albumSongs = storeAlbum.getSongs();
        assertFalse(albumSongs.isEmpty());
        Song toRate = albumSongs.get(0);
        library.rateSong(toRate, 4);

        library.createPlayList("AdelePlaylist");
        PlayList playlist = library.getPlayList("AdelePlaylist");
        playlist.addSong(toRate);

        userManager.saveUserLibrary(library);
        LibraryModel loaded = new LibraryModel();
        userManager.loadUserLibrary(loaded);

        Map<Song, Integer> ratedMap = loaded.getAllRatedSongs();
        boolean foundRatedSong = false;
        for (Map.Entry<Song, Integer> e : ratedMap.entrySet()) {
            Song s = e.getKey();
            if (s.getTitle().equalsIgnoreCase(toRate.getTitle())
                    && s.getArtist().equalsIgnoreCase(toRate.getArtist())) {
                assertEquals(4, e.getValue().intValue());
                foundRatedSong = true;
            }
        }
        assertTrue(foundRatedSong);

        PlayList loadedPL = loaded.getPlayList("AdelePlaylist");
        assertNotNull(loadedPL);
        assertEquals("AdelePlaylist", loadedPL.getName());
        assertEquals(1, loadedPL.getSongs().size());

        Song loadedSong = loadedPL.getSongs().get(0);
        assertEquals(toRate.getTitle(), loadedSong.getTitle());
        assertEquals(toRate.getArtist(), loadedSong.getArtist());
    }
    
    @Test
    void testMultipleUsers() throws IOException {
        assertTrue(userManager.createUser("alice", "passwordA"));
        assertTrue(userManager.authenticate("alice", "passwordA"));

        List<Song> rollingDeepSongs = musicStore.getSongsByTitle("Rolling in the Deep");
        assertFalse(rollingDeepSongs.isEmpty());
        Song rollingDeep = rollingDeepSongs.get(0);

        library.addSong(rollingDeep);
        userManager.saveUserLibrary(library);

        userManager.logout();

        assertTrue(userManager.createUser("bob", "passwordB"));
        assertTrue(userManager.authenticate("bob", "passwordB"));

        library = new LibraryModel();

        List<Song> lullabySongs = musicStore.getSongsByTitle("Lullaby");
        assertFalse(lullabySongs.isEmpty());
        Song lullaby = lullabySongs.get(0);

        library.addSong(lullaby);
        userManager.saveUserLibrary(library);

        userManager.logout();

        assertTrue(userManager.authenticate("alice", "passwordA"));
        LibraryModel aliceLoaded = new LibraryModel();
        userManager.loadUserLibrary(aliceLoaded);

        assertFalse(aliceLoaded.getSongs().isEmpty());
        assertTrue(aliceLoaded.getSongsByTitle("Rolling in the Deep").size() >= 1);
        assertTrue(aliceLoaded.getSongsByTitle("Lullaby").isEmpty());

        userManager.logout();

        assertTrue(userManager.authenticate("bob", "passwordB"));
        LibraryModel bobLoaded = new LibraryModel();
        userManager.loadUserLibrary(bobLoaded);

        assertFalse(bobLoaded.getSongs().isEmpty());
        assertTrue(bobLoaded.getSongsByTitle("Lullaby").size() >= 1);
        assertTrue(bobLoaded.getSongsByTitle("Rolling in the Deep").isEmpty());
    }
    
    @Test
    void testLoadAndSaveUserLibrary_PlayedSongsArePersisted() throws IOException {
        userManager.createUser("musicFan", "pass123");
        assertTrue(userManager.authenticate("musicFan", "pass123"));

        Album storeAlbum = musicStore.getAlbumByTitle("21");
        assertNotNull(storeAlbum);
        library.addAlbum(storeAlbum);
        Song someSong = storeAlbum.getSongs().get(0);

        library.playSong(someSong.getTitle(), someSong.getArtist());
        library.playSong(someSong.getTitle(), someSong.getArtist());

        userManager.saveUserLibrary(library);

        LibraryModel loaded = new LibraryModel();
        userManager.loadUserLibrary(loaded);

        List<Song> loadedRecent = loaded.getRecentlyPlayedSongs();
        assertFalse(loadedRecent.isEmpty());

        assertEquals(someSong.getTitle(), loadedRecent.get(0).getTitle());

        List<Song> loadedFreq = loaded.getFrequentlyPlayedSongs();
        assertFalse(loadedFreq.isEmpty());
        assertEquals(someSong.getTitle(), loadedFreq.get(0).getTitle());

        assertFalse(loaded.getSongs().isEmpty());
    }
}