package view;

import model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class View {
	private final LibraryModel library;
    private final MusicStore store;
    private final UserManager userManager;
    private final Scanner scanner;

    /**
     * View that interacts with the user
     *
     * @pre library != null && store != null && userManager != null
     */
    public View(LibraryModel library, MusicStore store, UserManager userManager) {
        this.library = library;
        this.store = store;
        this.userManager = userManager;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Presents a menu of commands to the user
     */
    public void run() {
        System.out.println("Welcome to the Music Library Application!");
        String command;

        do {
            printMenu();
            System.out.print("Enter your choice (or 'exit' to quit): ");
            command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "1":
                    searchStoreForSongTitle();
                    break;
                case "2":
                    searchStoreForSongArtist();
                    break;
                case "3":
                    searchStoreForAlbumTitle();
                    break;
                case "4":
                    searchStoreForAlbumArtist();
                    break;
                case "5":
                    searchLibraryForSongTitle();
                    break;
                case "6":
                    searchLibraryForSongArtist();
                    break;
                case "7":
                    searchLibraryForAlbumTitle();
                    break;
                case "8":
                    searchLibraryForAlbumArtist();
                    break;
                case "9":
                    searchLibraryForPlaylist();
                    break;
                case "a":
                    addSongToLibrary();
                    break;
                case "b":
                    addAlbumToLibrary();
                    break;
                case "c":
                    listLibraryItems();
                    break;
                case "d":
                    createPlaylist();
                    break;
                case "e":
                    addSongToPlaylist();
                    break;
                case "f":
                    removeSongFromPlaylist();
                    break;
                case "g":
                    markSongAsFavorite();
                    break;
                case "h":
                    rateSong();
                    break;
                case "i":
                    playSongInLibrary();
                    break;
                case "j":
                    removeSongFromLibrary();
                    break;
                case "k":
                    removeAlbumFromLibrary();
                    break;
                case "l":
                    shuffleLibrarySongs();
                    break;
                case "m":
                    shufflePlaylist();
                    break;
                case "n":
                    searchLibraryByGenre();
                    break;
                case "o":
                    getAlbumInfoForLibrarySong();
                    break;
                case "p":
                    getSortedLibrarySongs();
                    break;
                case "q":
                    library.updateAutoPlaylists();
                    System.out.println("Auto-playlists updated!");
                    break;
                case "r":
                    showAutoPlaylists();
                    break;

                case "exit":
                    System.out.println("Exiting the application...");
                    break;
                default:
                    System.out.println("Unrecognized command. Please try again.");
            }
            System.out.println();
        } while (!command.equals("exit"));

        System.out.println("Goodbye!");
    }

    /**
     * Menu
     * 
     * This menu was generated by ChatGPT, OpenAI, March 24, 2025
     */
    private void printMenu() {
        System.out.println("-------------------------------------------------");
        System.out.println("                MAIN MENU OPTIONS");
        System.out.println("-------------------------------------------------");
        System.out.println("  Store Searches:");
        System.out.println("    1) Search Store by Song Title");
        System.out.println("    2) Search Store by Song Artist");
        System.out.println("    3) Search Store by Album Title");
        System.out.println("    4) Search Store by Album Artist");
        System.out.println();
        System.out.println("  Library Searches:");
        System.out.println("    5) Search Library by Song Title");
        System.out.println("    6) Search Library by Song Artist");
        System.out.println("    7) Search Library by Album Title");
        System.out.println("    8) Search Library by Album Artist");
        System.out.println("    9) Search Library for Playlist by Name");
        System.out.println();
        System.out.println("  Library Modifications:");
        System.out.println("    a) Add Song to Library (from Store)");
        System.out.println("    b) Add Album to Library (from Store)");
        System.out.println("    c) List Library items");
        System.out.println("    d) Create Playlist");
        System.out.println("    e) Add Song to a Playlist");
        System.out.println("    f) Remove Song from a Playlist");
        System.out.println("    g) Mark Song as Favorite");
        System.out.println("    h) Rate a Song");
        System.out.println("    i) Play a Song (tracks plays)");
        System.out.println("    j) Remove a Song from Library");
        System.out.println("    k) Remove an Album from Library");
        System.out.println("    l) Shuffle your library songs");
        System.out.println("    m) Shuffle a playlist");
        System.out.println("    n) Search Library by Genre");
        System.out.println("    o) Get Album Info for a Song (Library)");
        System.out.println("    p) Get Sorted List of Songs (by title, artist, or rating)");
        System.out.println("    q) Update Auto-Playlists");
        System.out.println("    r) Show Auto-Playlists");
        System.out.println();
        System.out.println("  Type 'exit' to quit.");
        System.out.println("-------------------------------------------------");
    }

    /**
     * --- STORE SEARCHES ---
     * These methods use the MusicStore to search for songs/albums in the "database."
     * 
     * The general structure for the store searches was outlined by ChatGPT, OpenAI, February 27, 2025
     */

    private void searchStoreForSongTitle() {
        System.out.print("Enter song title to search in the store: ");
        String title = scanner.nextLine().trim();
        List<Song> songs = store.getSongsByTitle(title);
        if (songs.isEmpty()) {
            System.out.println("No songs found with title \"" + title + "\" in the store.");
        } else {
            System.out.println("Found the following song(s) in the store:");
            for (Song s : songs) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist() 
                                   + " (album: " + s.getAlbumTitle() + ")");
            }
        }
    }

    private void searchStoreForSongArtist() {
        System.out.print("Enter artist name to search in the store: ");
        String artist = scanner.nextLine().trim();
        List<Song> songs = store.getSongsByArtist(artist);
        if (songs.isEmpty()) {
            System.out.println("No songs found by artist \"" + artist + "\" in the store.");
        } else {
            System.out.println("Found the following song(s) in the store:");
            for (Song s : songs) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist() 
                                   + " (album: " + s.getAlbumTitle() + ")");
            }
        }
    }

    private void searchStoreForAlbumTitle() {
        System.out.print("Enter album title to search in the store: ");
        String title = scanner.nextLine().trim();
        Album album = store.getAlbumByTitle(title);
        if (album == null) {
            System.out.println("No album found with title \"" + title + "\" in the store.");
        } else {
            printAlbumInfo(album);
        }
    }

    private void searchStoreForAlbumArtist() {
        System.out.print("Enter album artist to search in the store: ");
        String artist = scanner.nextLine().trim();
        List<Album> albums = store.getAlbumsByArtist(artist);
        if (albums.isEmpty()) {
            System.out.println("No albums found for artist \"" + artist + "\" in the store.");
        } else {
            System.out.println("Found the following album(s) by " + artist + ":");
            for (Album album : albums) {
                printAlbumInfo(album);
            }
        }
    }

    /**
     * --- LIBRARY SEARCHES ---
     * These methods search the user's personal library (LibraryModel).
     * 
     * The general structure for the library searches was outlined by ChatGPT, OpenAI, February 27, 2025
     */

    private void searchLibraryForSongTitle() {
        System.out.print("Enter song title to search in your library: ");
        String title = scanner.nextLine().trim();
        List<Song> songs = library.getSongsByTitle(title);
        if (songs.isEmpty()) {
            System.out.println("No songs found with title \"" + title + "\" in your library.");
        } else {
            System.out.println("Found the following song(s) in your library:");
            for (Song s : songs) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist() 
                                   + " (album: " + s.getAlbumTitle() + ")");
            }
        }
    }

    private void searchLibraryForSongArtist() {
        System.out.print("Enter artist name to search in your library: ");
        String artist = scanner.nextLine().trim();
        List<Song> songs = library.getSongsByArtist(artist);
        if (songs.isEmpty()) {
            System.out.println("No songs found by \"" + artist + "\" in your library.");
        } else {
            System.out.println("Found the following song(s) in your library:");
            for (Song s : songs) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist() 
                                   + " (album: " + s.getAlbumTitle() + ")");
            }
        }
    }

    private void searchLibraryForAlbumTitle() {
        System.out.print("Enter album title to search in your library: ");
        String title = scanner.nextLine().trim();
        List<Album> albums = library.getAlbumsByTitle(title);
        if (albums.isEmpty()) {
            System.out.println("No albums found with title \"" + title + "\" in your library.");
        } else {
            System.out.println("Found the following album(s) in your library:");
            for (Album album : albums) {
                printAlbumInfo(album);
            }
        }
    }

    private void searchLibraryForAlbumArtist() {
        System.out.print("Enter album artist to search in your library: ");
        String artist = scanner.nextLine().trim();
        List<Album> albums = library.getAlbumsByArtist(artist);
        if (albums.isEmpty()) {
            System.out.println("No albums found by \"" + artist + "\" in your library.");
        } else {
            System.out.println("Found the following album(s) in your library:");
            for (Album album : albums) {
                printAlbumInfo(album);
            }
        }
    }

    private void searchLibraryForPlaylist() {
        System.out.print("Enter the playlist name to search in your library: ");
        String name = scanner.nextLine().trim();
        PlayList playlist = library.getPlayList(name);
        if (playlist == null) {
            System.out.println("No playlist named \"" + name + "\" in your library.");
        } else {
            System.out.println("Playlist \"" + playlist.getName() + "\" found. Songs:");
            for (Song s : playlist.getSongs()) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist());
            }
        }
    }

    /**
     * --- LIBRARY MODIFICATIONS ---
     * Methods for adding songs/albums to the library, creating playlists, etc.
     * 
     * The general structure for some of the library modifications was outlined by ChatGPT, OpenAI, February 27, 2025
     */

    private void addSongToLibrary() {
        System.out.println("To add a song from the store to your library:");
        System.out.print("  Enter the song title: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Enter the artist: ");
        String artist = scanner.nextLine().trim();

        // Look up song in store
        List<Song> allMatches = store.getSongsByTitle(title);
	    List<Song> songs = new ArrayList<>(allMatches);
	    songs.removeIf(s -> !s.getArtist().equalsIgnoreCase(artist));

        if (songs.isEmpty()) {
            System.out.println("Could not find that song in the store.");
            return;
        }

        // Add first (or all matches)
        for (Song s : songs) {
            library.addSong(s);
        }
        System.out.println("Song(s) successfully added to your library!");
    }

    private void addAlbumToLibrary() {
        System.out.println("To add an album from the store to your library:");
        System.out.print("  Enter the album title: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Enter the artist: ");
        String artist = scanner.nextLine().trim();

        // Look up album in store
        Album album = store.getAlbumByTitle(title);
        if (album == null || !album.getArtist().equalsIgnoreCase(artist)) {
        	List<Album> unmodifiable = store.getAlbumsByArtist(artist);
            List<Album> possibleMatches = new ArrayList<>(unmodifiable);
            possibleMatches.removeIf(a -> !a.getTitle().equalsIgnoreCase(title));
            if (possibleMatches.isEmpty()) {
                System.out.println("Could not find that album in the store.");
                return;
            }
            album = possibleMatches.get(0);
        }

        library.addAlbum(album);
        System.out.println("Album and its songs successfully added to your library!");
    }

    private void listLibraryItems() {
        System.out.println("Your Library Contents:");

        System.out.println("  Songs:");
        for (Song s : library.getSongs()) {
            System.out.println("    - " + s.getTitle() + " by " + s.getArtist() 
                               + " (album: " + s.getAlbumTitle() + ")");
        }
        System.out.println();

        System.out.println("  Artists:");
        for (String artist : library.getArtists()) {
            System.out.println("    - " + artist);
        }
        System.out.println();

        System.out.println("  Albums:");
        for (Album album : library.getAlbums()) {
            System.out.println("    - " + album.getTitle() + " by " + album.getArtist()
                               + " (" + album.getYear() + ", " + album.getGenre() + ")");
        }
        System.out.println();

        System.out.println("  Playlists:");
        for (PlayList p : library.getPlayLists()) {
            System.out.println("    - " + p.getName());
        }
        System.out.println();

        System.out.println("  Favorite Songs:");
        for (Song fav : library.getFavoriteSongs()) {
            System.out.println("    - " + fav.getTitle() + " by " + fav.getArtist());
        }
    }

    private void createPlaylist() {
        System.out.print("Enter a name for the new playlist: ");
        String name = scanner.nextLine().trim();
        library.createPlayList(name);
        System.out.println("Playlist \"" + name + "\" created successfully!");
    }

    private void addSongToPlaylist() {
        System.out.print("Enter the playlist name: ");
        String playlistName = scanner.nextLine().trim();

        PlayList playlist = library.getPlayList(playlistName);
        if (playlist == null) {
            System.out.println("No playlist named \"" + playlistName + "\" in your library.");
            return;
        }

        System.out.print("Enter the song title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter the artist: ");
        String artist = scanner.nextLine().trim();

        // Look for song in library
        List<Song> matches = library.getSongsByTitle(title);
        matches.removeIf(s -> !s.getArtist().equalsIgnoreCase(artist));

        if (matches.isEmpty()) {
            System.out.println("No matching song found in your library. Please add the song to the library first.");
            return;
        }

        // Add all matches (or just first)
        for (Song s : matches) {
            playlist.addSong(s);
        }

        System.out.println("Song(s) added to playlist \"" + playlistName + "\" successfully!");
    }

    private void removeSongFromPlaylist() {
        System.out.print("Enter the playlist name: ");
        String playlistName = scanner.nextLine().trim();

        PlayList playlist = library.getPlayList(playlistName);
        if (playlist == null) {
            System.out.println("No playlist named \"" + playlistName + "\" in your library.");
            return;
        }

        System.out.print("Enter the song title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter the artist: ");
        String artist = scanner.nextLine().trim();

        List<Song> toRemove = playlist.getSongs();
        toRemove.removeIf(s -> !(s.getTitle().equalsIgnoreCase(title)
                                && s.getArtist().equalsIgnoreCase(artist)));

        if (toRemove.isEmpty()) {
            System.out.println("No matching song in \"" + playlistName + "\" playlist.");
            return;
        }

        // Remove all
        for (Song s : toRemove) {
            playlist.removeSong(s);
        }

        System.out.println("Song(s) removed from playlist \"" + playlistName + "\" successfully!");
    }

    private void markSongAsFavorite() {
        System.out.println("Mark a song in your library as favorite.");
        System.out.print("  Enter the song title: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Enter the artist: ");
        String artist = scanner.nextLine().trim();

        List<Song> matches = library.getSongsByTitle(title);
        matches.removeIf(s -> !s.getArtist().equalsIgnoreCase(artist));

        if (matches.isEmpty()) {
            System.out.println("No matching song found in your library.");
            return;
        }

        for (Song s : matches) {
            library.markSongAsFavorite(s);
        }
        System.out.println("Song(s) marked as favorite!");
    }

    private void rateSong() {
        System.out.println("Rate a song in your library (1-5). Rating of 5 automatically marks it favorite.");
        System.out.print("  Song title: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Artist: ");
        String artist = scanner.nextLine().trim();
        System.out.print("  Rating (1-5): ");
        String ratingStr = scanner.nextLine().trim();

        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid rating. Must be an integer 1-5.");
            return;
        }

        List<Song> matches = library.getSongsByTitle(title);
        matches.removeIf(s -> !s.getArtist().equalsIgnoreCase(artist));

        if (matches.isEmpty()) {
            System.out.println("No matching song found in your library.");
            return;
        }

        boolean success = false;
        for (Song s : matches) {
            boolean rated = library.rateSong(s, rating);
            if (rated) success = true;
        }

        if (success) {
            System.out.println("Song(s) rated successfully!");
        } else {
            System.out.println("Failed to rate song(s). Check if the song is in your library.");
        }
    }
    
    /**
     * --- LA 2 ADDITIONS ---
     * Methods for playing, removing, searching, and shuffling songs/albums/playlists in the library.
     * 
     * The general structure for some of the LA 2 additions was outlined by ChatGPT, OpenAI, March 24, 2025
     */
    
    private void playSongInLibrary() {
        System.out.println("Play a song from your library.");
        System.out.print("  Song title: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Artist: ");
        String artist = scanner.nextLine().trim();

        library.playSong(title, artist);
        System.out.println("Song play recorded (if it exists in your library).\n");
    }
    
    private void removeSongFromLibrary() {
        System.out.println("Remove a song from your library.");
        System.out.print("  Song title: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Artist: ");
        String artist = scanner.nextLine().trim();

        List<Song> matches = library.getSongsByTitle(title);
        matches.removeIf(s -> !s.getArtist().equalsIgnoreCase(artist));

        if (matches.isEmpty()) {
            System.out.println("No matching song found in your library.");
            return;
        }

        for (Song s : matches) {
            library.removeSong(s);
            System.out.println("Removed '" + s.getTitle() + "' by " + s.getArtist() + " from the library.");
        }
    }
    
    private void removeAlbumFromLibrary() {
        System.out.println("Remove an album from your library.");
        System.out.print("  Album title: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Artist: ");
        String artist = scanner.nextLine().trim();

        List<Album> matches = library.getAlbumsByTitle(title);
        matches.removeIf(a -> !a.getArtist().equalsIgnoreCase(artist));

        if (matches.isEmpty()) {
            System.out.println("No matching album found in your library.");
            return;
        }

        for (Album a : matches) {
            library.removeAlbum(a);
            System.out.println("Removed album '" + a.getTitle() + "' by " + a.getArtist() + " from the library.");
        }
    }
    
    private void shuffleLibrarySongs() {
        System.out.println("Shuffled Library Songs:");
        List<Song> shuffled = library.getShuffledSongs();
        for (Song s : shuffled) {
            System.out.println("  - " + s.getTitle() + " by " + s.getArtist());
        }
    }
    
    private void shufflePlaylist() {
        System.out.print("Enter the playlist name to shuffle: ");
        String playlistName = scanner.nextLine().trim();
        PlayList playlist = library.getPlayList(playlistName);
        if (playlist == null) {
            System.out.println("No playlist named \"" + playlistName + "\" in your library.");
            return;
        }
        System.out.println("Shuffled Playlist \"" + playlistName + "\":");
        List<Song> shuffled = library.shuffleSongsInPlayList(playlist);
        for (Song s : shuffled) {
            System.out.println("  - " + s.getTitle() + " by " + s.getArtist());
        }
    }
    
    private void searchLibraryByGenre() {
        System.out.print("Enter the genre to search in your library: ");
        String genre = scanner.nextLine().trim();
        List<Song> results = library.getSongsByGenre(genre);
        if (results.isEmpty()) {
            System.out.println("No songs found in the \"" + genre + "\" genre.");
        } else {
            System.out.println("Found the following \"" + genre + "\" song(s) in your library:");
            for (Song s : results) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist());
            }
        }
    }

    private void getAlbumInfoForLibrarySong() {
        System.out.println("Enter the Song to see its full album info (from the store).");
        System.out.print("  Song title: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Artist: ");
        String artist = scanner.nextLine().trim();
        Album alb = library.getAlbumInfoForSong(title, artist);
        if (alb == null) {
            System.out.println("No matching album found in the store for that song.");
            return;
        }
        System.out.println("\n--- ALBUM INFO ---");
        printAlbumInfo(alb);
        boolean inLib = library.isAlbumInLibrary(alb.getTitle(), alb.getArtist());
        if (inLib) {
            System.out.println("(This album is already in your library, at least partially.)\n");
        } else {
            System.out.println("(This album is not yet in your library.)\n");
        }
    }

    private void getSortedLibrarySongs() {
        System.out.println("Sort songs in your library:");
        System.out.println("  1) By Title");
        System.out.println("  2) By Artist");
        System.out.println("  3) By Rating");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();

        List<Song> sorted = new ArrayList<>();
        switch (choice) {
            case "1":
                sorted = library.getSongsSortedByTitle();
                break;
            case "2":
                sorted = library.getSongsSortedByArtist();
                break;
            case "3":
                sorted = library.getSongsSortedByRating();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("\nSorted Songs:");
        for (Song s : sorted) {
            Integer rating = library.getAllRatedSongs().getOrDefault(s, 0);
            System.out.println("  " + s.getTitle() + " by " + s.getArtist()
                               + " (Rating: " + rating + ")");
        }
    }

    private void showAutoPlaylists() {
        System.out.println("\nAUTO-PLAYLIST: Favorite Songs (Auto)");
        PlayList fav = library.getFavoritesAutoPlaylist();
        if (fav != null) {
            for (Song s : fav.getSongs()) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist());
            }
        }

        System.out.println("\nAUTO-PLAYLIST: Top Rated (Auto)");
        PlayList top = library.getTopRatedAutoPlaylist();
        if (top != null) {
            for (Song s : top.getSongs()) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist());
            }
        }
        
        System.out.println("\nAUTO-PLAYLIST: Most Frequently Played (Auto)");
        PlayList freq = library.getFrequentlyPlayedPlayList();
        if (freq != null) {
            for (Song s : freq.getFrequentlyPlayedSongs()) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist() + ": " + s.getPlayCount() + " time(s) played");
            }
        }
        
        System.out.println("\nAUTO-PLAYLIST: Most Recently Played (Auto)");
        PlayList rec = library.getRecentlyPlayedPlayList();
        if (rec != null) {
            for (Song s : rec.getRecentlyPlayedSongs()) {
                System.out.println("  - " + s.getTitle() + " by " + s.getArtist());
            }
        }

        System.out.println("\nAUTO-PLAYLISTS by Genre:");
        Set<String> uniqueGenres = new HashSet<>();
        for (Album a : library.getAlbums()) {
            uniqueGenres.add(a.getGenre().toLowerCase());
        }

        for (String g : uniqueGenres) {
            PlayList gp = library.getGenreAutoPlaylist(g);
            if (gp != null) {
                System.out.println("  " + gp.getName() + ":");
                for (Song s : gp.getSongs()) {
                    System.out.println("     - " + s.getTitle() + " by " + s.getArtist());
                }
                System.out.println();
            }
        }
    }

    /**
     * Helper method to print album info
     */
    private void printAlbumInfo(Album album) {
        System.out.println("Album: " + album.getTitle());
        System.out.println("  Artist: " + album.getArtist());
        System.out.println("  Genre: " + album.getGenre());
        System.out.println("  Year: " + album.getYear());
        System.out.println("  Tracks:");
        int i = 1;
        for (Song s : album.getSongs()) {
            System.out.println("    " + (i++) + ". " + s.getTitle());
        }
        System.out.println();
    }
}
