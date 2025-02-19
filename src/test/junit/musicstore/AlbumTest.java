package musicstore;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * JUnit tests for Album class
 */
class AlbumTest {
    private final Song song1 = new Song("Hold On", "Alabama Shakes", "Boys & Girls");
    private final Song song2 = new Song("Hang Loose", "Alabama Shakes", "Boys & Girls");
	private final Album album = new Album("Boys & Girls", "Alabama Shakes", "Alternative", 2012, new ArrayList<>(Arrays.asList(song1, song2)));

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
        ArrayList<Song> songs = album.getSongs();
        assertEquals(2, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
        
        songs.clear();
        assertEquals(2, album.getSongs().size());
    }
}
