package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * JUnit tests for PlayList class
 */
class PlayListTest {
    private PlayList playlist;
    private Song song1;
    private Song song2;

    /**
     * Constructs a new PlayList object before each test
     */
    @BeforeEach
    void setUp() {
        song1 = new Song("Hold On", "Alabama Shakes", "Boys & Girls");
        song2 = new Song("Hang Loose", "Alabama Shakes", "Boys & Girls");

        playlist = new PlayList("Test Playlist");
    }

    @Test
    public void testGetName() {
        assertEquals("Test Playlist", playlist.getName());
    }

    @Test
    public void testAddSong() {
        playlist.addSong(song1);
        playlist.addSong(song2);

        List<Song> songs = playlist.getSongs();
        assertEquals(2, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
    }

    @Test
    public void testRemoveSong() {
        playlist.addSong(song1);
        playlist.addSong(song2);
        
        playlist.removeSong(song1);
        List<Song> songs = playlist.getSongs();
        assertEquals(1, songs.size());
        assertFalse(songs.contains(song1));
        assertTrue(songs.contains(song2));
    }

    @Test
    public void testGetSongsImmutable() {
        playlist.addSong(song1);
        playlist.addSong(song2);
        List<Song> songs = playlist.getSongs();
        
        assertEquals(2, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
        
        songs.clear();
        assertEquals(2, playlist.getSongs().size());
    }
    
    @Test
    void testAddSongRecent() {
        for (int i = 1; i <= 12; i++) {
            Song s = new Song("Title" + i, "Artist" + i, "Album" + i);
            playlist.addSongRecent(s);
        }
        List<Song> recents = playlist.getRecentlyPlayedSongs();
        assertEquals(10, recents.size());

        assertEquals("Title12", recents.get(0).getTitle());
        assertEquals("Title3", recents.get(9).getTitle());
    }
    
    @Test
    void testAddSongFrequent() {
        song1.setPlayCount(5);
        song2.setPlayCount(10);

        playlist.addSongFrequent(song1);
        playlist.addSongFrequent(song2);

        for (int i = 4; i <= 11; i++) {
            Song s = new Song("Title" + i, "Artist" + i, "Album" + i);
            s.setPlayCount(i);
            playlist.addSongFrequent(s);
        }
        List<Song> freq = playlist.getFrequentlyPlayedSongs();
        assertEquals(10, freq.size());

        assertEquals("Title11", freq.get(0).getTitle()); 
        assertEquals("Hang Loose", freq.get(1).getTitle());
    }
    
    @Test
    void testGetShuffledSongs() {
        playlist.addSong(song1);
        playlist.addSong(song2);

        List<Song> shuffled = playlist.getShuffledSongs();
        assertEquals(2, shuffled.size());
        
        assertTrue(shuffled.contains(song1));
        assertTrue(shuffled.contains(song2));
    }
}