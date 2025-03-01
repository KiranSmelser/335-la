package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JUnit tests for Album class
 */
class AlbumTest {
	private Album album;
    private Song song1;
    private Song song2;
    private List<Song> songList;

    /**
     * Constructs a new Album object before each test
     */
    @BeforeEach
    void setUp() {
        song1 = new Song("Hold On", "Alabama Shakes", "Boys & Girls");
        song2 = new Song("Hang Loose", "Alabama Shakes", "Boys & Girls");

        songList = new ArrayList<>(Arrays.asList(song1, song2));

        album = new Album("Boys & Girls", "Alabama Shakes", "Alternative", 2012, songList);
    }

    @Test
    public void testGetTitle() {
        assertEquals("Boys & Girls", album.getTitle());
    }

    @Test
    public void testGetArtist() {
        assertEquals("Alabama Shakes", album.getArtist());
    }
    
    @Test
    public void testGetGenre() {
        assertEquals("Alternative", album.getGenre());
    }
    
    @Test
    public void testGetYear() {
        assertEquals(2012, album.getYear());
    }

    @Test
    public void testGetSongs() {
        List<Song> songs = album.getSongs();
        assertEquals(2, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
        
        songs.clear();
        assertEquals(2, album.getSongs().size());
    }
}
