package musicstore;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * JUnit tests for Song class
 */
class SongTest {
	private final Song song = new Song("Hold On", "Alabama Shakes", "Boys & Girls");
	
    @Test
    void testGetTitle() {
        assertEquals("Hold On", song.getTitle());
    }

    @Test
    void testGetArtist() {
        assertEquals("Alabama Shakes", song.getArtist());
    }

    @Test
    void testGetAlbumTitle() {
        assertEquals("Boys & Girls", song.getAlbumTitle());
    }
}
