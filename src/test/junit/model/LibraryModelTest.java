package musicstore;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * JUnit tests for LibraryModel class
 */
class LibraryModelTest {
	
    private LibraryModel library;
    private Song song1;
    private Song song2;
    private Song song3;
    private Album album;
    private PlayList playlist;
    

    @BeforeEach
    void setUp() {
    	
        library = new LibraryModel();

        song1 = new Song("Uh Oh", "Norah Jones", "Begin Again");
        song2 = new Song("Daydreamer", "Adele", "19");

        List<Song> albumSongs = new ArrayList<>();
        albumSongs.add(song1);
        albumSongs.add(song2);
        album = new Album("test album", "test artist", "test genre", 2000, albumSongs);
        library.addAlbum(album);
    }

    @Test
    void testAddSong() {
    	library.addSong(song1); library.addSong(song2);
        assertEquals(2, library.getSongTitles().size()); // song1 and song2 from album, plus song1 added separately
        assertTrue(library.getArtists().contains("Norah Jones"));
        assertTrue(library.getArtists().contains("Adele"));
    }

    @Test
    void testAddAlbum() {
        assertEquals(1, library.getAlbums().size());
        assertEquals("test album", library.getAlbums().get(0).getTitle());
    }

    @Test
    void testGetSongByTitle() {
        assertEquals(song1, library.getSongsByTitle("Uh Oh").get(0));
        assertEquals(song2, library.getSongsByTitle("Daydreamer").get(0));
        assertEquals(0, library.getSongsByTitle("Nonexisting Song").size());
    }

    @Test
    void testGetSongByArtist() {
        assertEquals(song1, library.getSongsByArtist("Norah Jones").get(0));
        assertEquals(song2, library.getSongsByArtist("Adele").get(0));
        assertEquals(0, library.getSongsByArtist("Nonexisting Artist").size());
    }

    @Test
    void testGetAlbumByTitle() {
        assertEquals(album, library.getAlbumsByTitle("test album").get(0));
        assertEquals(0, library.getAlbumsByTitle("Nonexistent Album").size());
    }

    @Test
    void testGetAlbumByArtist() {
        assertEquals(album, library.getAlbumsByArtist("test artist").get(0));
        assertEquals(0, library.getAlbumsByArtist("Nonexisting artist").size());
    }

    @Test
    void testRateSongValid() {
        assertTrue(library.rateSong(song1, 5));
        assertTrue(library.getFavoriteSongs().contains(song1));
    }

    @Test
    void testRateSongInvalidRating() {
        assertFalse(library.rateSong(song1, 6)); // Invalid rating
        assertFalse(library.rateSong(song2, -1)); // Invalid rating
    }

    @Test
    void testRateSongNotInLibrary() {
        assertFalse(library.rateSong(song3, 3)); // song3 was never added
    }

    @Test
    void testCreateAndRetrievePlaylist() {
        library.createPlayList("My Playlist");
        playlist = library.getPlayList("My Playlist");

        assertNotNull(playlist);
        assertEquals("My Playlist", playlist.getName());
        assertEquals(1, library.getPlayLists().size());
    }

    @Test
    void testAddSongToPlaylist() {
        library.createPlayList("My Playlist2");
        playlist = library.getPlayList("My Playlist2");
        assertNotNull(playlist);

        playlist.addSong(song1);
        assertEquals(1, playlist.getSongs().size());
        assertEquals(song1, playlist.getSongs().get(0));
    }

    @Test
    void testRemoveSongFromPlaylist() {
        library.createPlayList("My Playlist3");
        playlist = library.getPlayList("My Playlist3");
        playlist.addSong(song1);
        playlist.addSong(song2);

        playlist.removeSong(song1);
        assertEquals(1, playlist.getSongs().size());
        assertEquals(song2, playlist.getSongs().get(0));
    }

}