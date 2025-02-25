package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Album;
import model.MusicStore;
import model.Song;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for MusicStore class
 */
public class MusicStoreTest {

    private static final String ALBUMS_TXT_PATH = "src/main/resources/albums.txt";
    private static final String ALBUMS_DIR_PATH = "src/main/resources/albums";

    private MusicStore store;

    /**
     * Constructs a new MusicStore object before each test
     */
    @BeforeEach
    public void setUp() {
        try {
            store = new MusicStore(ALBUMS_TXT_PATH, ALBUMS_DIR_PATH);
        } catch (IOException e) {
            fail("Failed to load data from files: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------------
    // Album-Related Tests
    // ------------------------------------------------------------------------

    @Test
    public void testGetAlbumByTitle() {
        Album album = store.getAlbumByTitle("21");
        assertNotNull(album);
        assertEquals("21", album.getTitle());
        assertEquals("Adele", album.getArtist());
        
        Album album2 = store.getAlbumByTitle("");
        assertNull(album2);
    }

    @Test
    public void testGetAlbumsByArtist() {
    	// Test for single Album
        List<Album> albums = store.getAlbumsByArtist("Leonard Cohen");
        assertNotNull(albums);
        assertEquals(1, albums.size());
        assertEquals("Old Ideas", albums.get(0).getTitle());
        
        // Test for multiple Albums
        List<Album> albums2 = store.getAlbumsByArtist("Adele");
        assertNotNull(albums2);
        assertTrue(albums2.size() >= 2);
        boolean has21 = albums2.stream().anyMatch(a -> a.getTitle().equalsIgnoreCase("21"));
        boolean has25 = albums2.stream().anyMatch(a -> a.getTitle().equalsIgnoreCase("25"));
        assertTrue(has21 && has25);
    }

    // ------------------------------------------------------------------------
    // Song-Related Tests
    // ------------------------------------------------------------------------

    @Test
    public void testGetSongsByTitle() {
        // Test for single Song
        List<Song> songs = store.getSongsByTitle("Rolling in the Deep");
        assertNotNull(songs);
        assertEquals(1, songs.size());
        Song song = songs.get(0);
        assertEquals("Rolling in the Deep", song.getTitle());
        assertEquals("Adele", song.getArtist());
        
        // Test for multiple Songs
        List<Song> songs2 = store.getSongsByTitle("Lullaby");
        assertNotNull(songs2);
        assertTrue(songs2.size() >= 2);
        
        // Test for Song that does not exist
        List<Song> songs3 = store.getSongsByTitle("Purple Rain");
        assertNotNull(songs3);
        assertTrue(songs3.isEmpty());
    }
    
    @Test
    public void testGetSongsByArtist() {
        // Test for single Song
        List<Song> songs = store.getSongsByArtist("Leonard Cohen");
        assertNotNull(songs);
        assertFalse(songs.isEmpty());
        
        // Test for multiple Songs
        List<Song> songs2 = store.getSongsByArtist("Adele");
        assertNotNull(songs2);
        assertTrue(songs2.size() > 10);
        
        // Test for Artist that does not exist
        List<Song> songs3 = store.getSongsByArtist("Prince");
        assertNotNull(songs3);
        assertTrue(songs3.isEmpty());
    }
}