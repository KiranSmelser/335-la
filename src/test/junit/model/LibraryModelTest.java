package model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
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
    private MusicStore store;
    

    @BeforeEach
    void setUp() throws IOException {
    	
        library = new LibraryModel();
        
        store = new MusicStore("src/main/resources/albums.txt", "src/main/resources/albums");
        library.setMusicStore(store);

        song1 = new Song("Uh Oh", "Norah Jones", "Begin Again");
        song2 = new Song("Daydreamer", "Adele", "19");
        song3 = new Song("Take It All", "Adele", "21");

        List<Song> albumSongs = new ArrayList<>();
        albumSongs.add(song1);
        albumSongs.add(song2);
        album = new Album("test album", "test artist", "test genre", 2000, albumSongs);
        library.addAlbum(album);
    }

    @Test
    void testAddSong() {
    	library.addSong(song1); library.addSong(song2);
        assertEquals(2, library.getSongTitles().size());
        assertTrue(library.getArtists().contains("Norah Jones"));
        assertTrue(library.getArtists().contains("Adele"));
        
    }
    
    @Test
    void testGetSong() {
    	assertEquals(2, library.getSongs().size());
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
        assertFalse(library.rateSong(song1, 6));
        assertFalse(library.rateSong(song2, -1));
    }

    @Test
    void testRateSongNotInLibrary() {
        assertFalse(library.rateSong(song3, 3));
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
    
    @Test
    void testmarkSongAsFavorite() { 
    	assertFalse(library.markSongAsFavorite(song3));
    	assertTrue(library.markSongAsFavorite(song1));
    	
    }
    
    @Test
    void testRemoveSong() {
        library.addSong(song2);
        assertTrue(library.getSongs().contains(song2));
        library.removeSong(song2);
        assertFalse(library.getSongs().contains(song2));
    }
    
    @Test
    void testRemoveAlbum() {
        assertTrue(library.getAlbums().contains(album));
        library.removeAlbum(album);
        assertFalse(library.getAlbums().contains(album));

        assertFalse(library.getSongs().contains(song1));
        assertFalse(library.getSongs().contains(song2));
    }
    
    @Test
    void testSorting() {
        library.addSong(song3);

        // Sort by title
        List<Song> byTitle = library.getSongsSortedByTitle();
        assertEquals("Daydreamer", byTitle.get(0).getTitle());
        assertEquals("Take It All", byTitle.get(1).getTitle());
        assertEquals("Uh Oh", byTitle.get(2).getTitle());

        // Sort by artist
        List<Song> byArtist = library.getSongsSortedByArtist();
        assertEquals("Adele", byArtist.get(0).getArtist());
        assertEquals("Adele", byArtist.get(1).getArtist());
        assertEquals("Norah Jones", byArtist.get(2).getArtist());

        library.rateSong(song3, 2);
        library.rateSong(song2, 5);
        library.rateSong(song1, 4);

        // Sort by rating ascending
        List<Song> byRating = library.getSongsSortedByRating();
        assertEquals(song3, byRating.get(0));
        assertEquals(song1, byRating.get(1));
        assertEquals(song2, byRating.get(2));
    }
    
    @Test
    void testPlaySong() {
        library.addSong(song3);

        library.playSong("Uh Oh", "Norah Jones");

        List<Song> recent = library.getRecentlyPlayedSongs();
        assertEquals(1, recent.size());
        assertEquals("Uh Oh", recent.get(0).getTitle());

        List<Song> frequent = library.getFrequentlyPlayedSongs();
        assertEquals(1, frequent.size());
        assertEquals("Uh Oh", frequent.get(0).getTitle());

        library.playSong("Take It All", "Adele");
        assertEquals(2, library.getRecentlyPlayedSongs().size());
        assertEquals("Take It All", library.getRecentlyPlayedSongs().get(0).getTitle());
        assertEquals(2, library.getFrequentlyPlayedSongs().size());

        library.playSong("Uh Oh", "Norah Jones");
        library.playSong("Uh Oh", "Norah Jones");

        List<Song> freq2 = library.getFrequentlyPlayedSongs();
        assertEquals("Uh Oh", freq2.get(0).getTitle());
    }
    
    @Test
    void testShuffle() {
        library.addSong(song3);
        List<Song> shuffled = library.getShuffledSongs();
        assertEquals(3, shuffled.size());

        assertTrue(shuffled.contains(song1));
        assertTrue(shuffled.contains(song2));
        assertTrue(shuffled.contains(song3));
    }
    
    @Test
    void testGetAlbumInfoForSong_dPart() {
        Album storeAlbum = library.getAlbumInfoForSong("Daydreamer", "Adele");
        
        if (storeAlbum != null) {
            assertEquals("19", storeAlbum.getTitle()); 
            assertEquals("Adele", storeAlbum.getArtist());

            boolean inLibrary = library.isAlbumInLibrary(storeAlbum.getTitle(), storeAlbum.getArtist());

            assertFalse(inLibrary);
        }
        else {
            assertNull(storeAlbum);
        }
    }
    
    @Test
    void testAddSongCreatesPartialAlbum() {
        assertFalse(library.getSongs().contains(song3));
        assertEquals(1, library.getAlbums().size(), "Only 'test album' so far");

        library.addSong(song3);

        assertTrue(library.getSongs().contains(song3));
        assertEquals(2, library.getAlbums().size(), "We should have the old 'test album' plus new partial album");
        
        Album partialAdeleAlbum = null;
        for (Album a : library.getAlbums()) {
            if (a.getTitle().equalsIgnoreCase("21") && a.getArtist().equalsIgnoreCase("Adele")) {
                partialAdeleAlbum = a;
                break;
            }
        }
        assertNotNull(partialAdeleAlbum);
        assertEquals(1, partialAdeleAlbum.getSongs().size(), "Should contain only 'Take It All'");
        assertEquals("Take It All", partialAdeleAlbum.getSongs().get(0).getTitle());
    }
    
    @Test
    void testGetSongsByGenre() {
        List<Song> found = library.getSongsByGenre("test genre");
        assertEquals(2, found.size());
        assertTrue(found.contains(song1));
        assertTrue(found.contains(song2));

        List<Song> empty = library.getSongsByGenre("Rock");
        assertTrue(empty.isEmpty());
    }
    
    @Test
    void testAutoPlaylists() {
        library.addSong(song2);
        library.markSongAsFavorite(song2);
        
        library.rateSong(song1, 4);

        for (int i = 0; i < 9; i++) {
            Song additional = new Song("test song " + i, "test artist", "test album");
            library.addSong(additional);
        }
        
        library.updateAutoPlaylists();
        
        PlayList favesAuto = library.getFavoritesAutoPlaylist();
        assertNotNull(favesAuto);

        assertEquals(1, favesAuto.getSongs().size());
        assertEquals(song2.getTitle(), favesAuto.getSongs().get(0).getTitle());

        PlayList topRated = library.getTopRatedAutoPlaylist();
        assertNotNull(topRated);

        boolean hasSong1 = topRated.getSongs().contains(song1);
        boolean hasSong2 = topRated.getSongs().contains(song2); 
        assertTrue(hasSong1 || hasSong2);

        PlayList genreAuto = library.getGenreAutoPlaylist("test genre");

        assertNotNull(genreAuto);
        assertEquals(11, genreAuto.getSongs().size());
    }
}
