package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * JUnit tests for Song class
 */
class SongTest {
	private Song song;
	private Song song2;
	
	/**
     * Constructs a new Song object before each test
     */
	@BeforeEach
    void setUp() {
        song = new Song("Hold On", "Alabama Shakes", "Boys & Girls");
        song2 = new Song("Traveling Man", "Dolly Parton", "Coat of Many Colors");
    }
	
    @Test
    void testGetTitle() {
        assertEquals("Hold On", song.getTitle());
        assertEquals("Traveling Man", song2.getTitle());
    }

    @Test
    void testGetArtist() {
        assertEquals("Alabama Shakes", song.getArtist());
        assertEquals("Dolly Parton", song2.getArtist());
    }

    @Test
    void testGetAlbumTitle() {
        assertEquals("Boys & Girls", song.getAlbumTitle());
        assertEquals("Coat of Many Colors", song2.getAlbumTitle());
    }
    
    @Test
    void testPlayCountAndPlayMethod() {
        assertEquals(0, song.getPlayCount());
        song.play();
        song.play();
        assertEquals(2, song.getPlayCount());

        assertEquals(0, song2.getPlayCount());
    }

    @Test
    void testSetPlayCount() {
        song.setPlayCount(5);
        assertEquals(5, song.getPlayCount());

        song.setPlayCount(-3);
        assertEquals(0, song.getPlayCount());
        
        song.setPlayCount(0);
        assertEquals(0, song.getPlayCount());
    }
}
