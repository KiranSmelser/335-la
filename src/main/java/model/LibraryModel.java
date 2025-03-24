package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class LibraryModel {

	private final List<Song> songs;
	private final Set<String> artists;
	private final List<Album> albums;
	private final List<Song> favoriteSongs;
	private final List<PlayList> playlists;
	private final Map<Song, Integer> ratedSongs;
	private final PlayList recentlyPlayedSongs;
	private final PlayList frequentlyPlayedSongs;
	private MusicStore store;
    private PlayList favoritesAutoPlaylist;
    private PlayList topRatedAutoPlaylist;
    private final Map<String, PlayList> genrePlaylists;

    public LibraryModel() {
        songs = new ArrayList<>();
        artists = new HashSet<>();
        albums = new ArrayList<>();
        favoriteSongs = new ArrayList<>();
        playlists = new ArrayList<>();
        ratedSongs = new HashMap<>();
        recentlyPlayedSongs = new PlayList("Most Recently Played Songs");
        frequentlyPlayedSongs = new PlayList("Most Frequently Played Songs");
        genrePlaylists = new HashMap<>();
    }
    
    /**
     * Allows LibraryModel to query MusicStore
     */
    public void setMusicStore(MusicStore store) {
        this.store = store;
    }
    
    public MusicStore getMusicStore() {
        return this.store;
    }
	
	/**
     * Adds a song to library if not already present
     */
    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            artists.add(song.getArtist());
        }
        Album libraryAlbum = findAlbumInLibrary(song.getAlbumTitle(), song.getArtist());
        if (libraryAlbum == null) {
            String genre = "Unknown";
            int year = 0;
            if (store != null) {
                Album storeAlbum = store.getAlbumByTitle(song.getAlbumTitle());
                if (storeAlbum != null && 
                    storeAlbum.getArtist().equalsIgnoreCase(song.getArtist())) {
                    genre = storeAlbum.getGenre();
                    year = storeAlbum.getYear();
                }
            }
            List<Song> partialSongs = new ArrayList<>();
            partialSongs.add(song);
            Album partialAlbum = new Album(song.getAlbumTitle(), song.getArtist(), genre, year, partialSongs);
            albums.add(partialAlbum);
        } else {
            List<Song> currentTracks = libraryAlbum.getSongs();
            if (!currentTracks.contains(song)) {
                albums.remove(libraryAlbum);
                List<Song> updatedSongs = new ArrayList<>(currentTracks);
                updatedSongs.add(song);
                Album updatedAlbum = new Album(
                    libraryAlbum.getTitle(),
                    libraryAlbum.getArtist(),
                    libraryAlbum.getGenre(),
                    libraryAlbum.getYear(),
                    updatedSongs
                );
                albums.add(updatedAlbum);
            }
        }
    }
    
    /**
     * Adds an album and all its songs to library if not already present
     */
    public void addAlbum(Album album) {
        Album existingAlbum = findAlbumInLibrary(album.getTitle(), album.getArtist());
        if (existingAlbum != null) {
            albums.remove(existingAlbum);

            List<Song> mergedSongs = new ArrayList<>(existingAlbum.getSongs());
            for (Song s : album.getSongs()) {
                if (!mergedSongs.contains(s)) {
                    mergedSongs.add(s);
                }
            }

            Album mergedAlbum = new Album(
                existingAlbum.getTitle(),
                existingAlbum.getArtist(),
                existingAlbum.getGenre(),
                existingAlbum.getYear(),
                mergedSongs
            );

            albums.add(mergedAlbum);

            for (Song s : mergedSongs) {
                if (!songs.contains(s)) {
                    songs.add(s);
                    artists.add(s.getArtist());
                }
            }
        }
        else {
            if (!albums.contains(album)) {
                albums.add(album);
            }
            for (Song s : album.getSongs()) {
                if (!songs.contains(s)) {
                    songs.add(s);
                    artists.add(s.getArtist());
                }
            }
        }
    }
    
    /**
     * Given title, artist returns album info
     */
    public Album getAlbumInfoForSong(String title, String artist) {
        List<Song> storeSongs = store.getSongsByTitle(title);
        for (Song s : storeSongs) {
            if (s.getArtist().equalsIgnoreCase(artist)) {
                Album storeAlb = store.getAlbumByTitle(s.getAlbumTitle());
                return storeAlb; 
            }
        }
        return null;
    }
    
    /**
     * Checks if album is in library
     */
    public boolean isAlbumInLibrary(String albumTitle, String artist) {
        return (findAlbumInLibrary(albumTitle, artist) != null);
    }
    
    /**
     * Returns all songs in library that match genre
     */
    public List<Song> getSongsByGenre(String genre) {
        List<Song> result = new ArrayList<>();
        for (Album alb : albums) {
            if (alb.getGenre().equalsIgnoreCase(genre)) {
                result.addAll(alb.getSongs());
            }
        }
        return result;
    }
    
    /**
     * Rebuilds auto-playlists
     */
    public void updateAutoPlaylists() {
        favoritesAutoPlaylist = new PlayList("Favorite Songs (Auto)");
        for (Song s : favoriteSongs) {
            favoritesAutoPlaylist.addSong(s);
        }

        topRatedAutoPlaylist = new PlayList("Top Rated (Auto)");
        for (Map.Entry<Song, Integer> e : ratedSongs.entrySet()) {
            if (e.getValue() >= 4) {
                topRatedAutoPlaylist.addSong(e.getKey());
            }
        }

        genrePlaylists.clear();
        Map<String, List<Song>> genreToSongs = new HashMap<>();

        for (Album alb : albums) {
            String g = alb.getGenre();
            List<Song> albumTracks = alb.getSongs();
            genreToSongs.computeIfAbsent(g, k -> new ArrayList<>()).addAll(albumTracks);
        }

        for (Map.Entry<String, List<Song>> ent : genreToSongs.entrySet()) {
            String genre = ent.getKey();
            List<Song> allSongs = ent.getValue();
            if (allSongs.size() >= 10) {
                PlayList gp = new PlayList(genre + " (Auto)");
                for (Song s : allSongs) {
                    gp.addSong(s);
                }
                genrePlaylists.put(genre.toLowerCase(), gp);
            }
        }
    }

    public PlayList getFavoritesAutoPlaylist() {
        return favoritesAutoPlaylist;
    }

    public PlayList getTopRatedAutoPlaylist() {
        return topRatedAutoPlaylist;
    }

    public PlayList getGenreAutoPlaylist(String genre) {
        return genrePlaylists.get(genre.toLowerCase());
    }

    private Album findAlbumInLibrary(String title, String artist) {
        for (Album a : albums) {
            if (a.getTitle().equalsIgnoreCase(title) && a.getArtist().equalsIgnoreCase(artist)) {
                return a;
            }
        }
        return null;
    }

	/**
     * Returns all Songs that match a given title
     */
    public List<Song> getSongsByTitle(String title) {
        List<Song> results = new ArrayList<>();
        for (Song song : songs) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                results.add(song);
            }
        }
        return results;
    }

    /**
     * Returns all Songs by a given artist
     */
    public List<Song> getSongsByArtist(String artist) {
        List<Song> results = new ArrayList<>();
        for (Song song : songs) {
            if (song.getArtist().equalsIgnoreCase(artist)) {
                results.add(song);
            }
        }
        return results;
    }

    /**
     * Returns all Albums that match a given title
     */
    public List<Album> getAlbumsByTitle(String title) {
        List<Album> results = new ArrayList<>();
        for (Album album : albums) {
            if (album.getTitle().equalsIgnoreCase(title)) {
                results.add(album);
            }
        }
        return results;
    }

    /**
     * Returns all Albums by a given artist
     */
    public List<Album> getAlbumsByArtist(String artist) {
        List<Album> results = new ArrayList<>();
        for (Album album : albums) {
            if (album.getArtist().equalsIgnoreCase(artist)) {
                results.add(album);
            }
        }
        return results;
    }
    
    public List<Song> getSongs() {
        return Collections.unmodifiableList(songs);
    }
	
	public List<String> getSongTitles() {
		List<String> songTitles = new ArrayList<>();
		for (Song song : songs) {
			songTitles.add(song.getTitle());
		}
		return songTitles;
	}
	
	public Set<String> getArtists() {
        return Collections.unmodifiableSet(artists);
    }
	
	public List<Album> getAlbums() {
        return Collections.unmodifiableList(albums);
    }
	
	public List<PlayList> getPlayLists() {
        return Collections.unmodifiableList(playlists);
    }
	
	public List<Song> getFavoriteSongs() {
        return Collections.unmodifiableList(favoriteSongs);
    }
	
	public Map<Song, Integer> getAllRatedSongs() {
	    return new HashMap<>(ratedSongs);
	}
	
	/**
     * Rates the song 1-5. If 5, adds to favorites
     */
    public boolean rateSong(Song song, int rate) {
        if (rate < 1 || rate > 5) {
            return false;
        }
        if (!songs.contains(song)) {
            return false;
        }
        ratedSongs.put(song, rate);

        if (rate == 5 && !favoriteSongs.contains(song)) {
            favoriteSongs.add(song);
        } else if (favoriteSongs.contains(song)) {
            favoriteSongs.remove(song);	// remove song from favorites if rating < 5
        }

        return true;
    }
    
    /**
     * Marks a given song as favorite
     */
    public boolean markSongAsFavorite(Song song) {
        if (!songs.contains(song)) {
            return false;
        }
        if (!favoriteSongs.contains(song)) {
            favoriteSongs.add(song);
        }
        return true;
    }
	
    public void createPlayList(String name) {
        PlayList playlist = new PlayList(name);
        playlists.add(playlist);
    }
	
	public PlayList getPlayList(String name) {
		for (PlayList playlist : playlists) {
			if (name.equals(playlist.getName())) return playlist;
		}
		return null;
	}
	
	public PlayList getRecentlyPlayedPlayList() {
	    return this.recentlyPlayedSongs;
	}

	public PlayList getFrequentlyPlayedPlayList() {
	    return this.frequentlyPlayedSongs;
	}
	
	public List<Song> getRecentlyPlayedSongs() {
	    return recentlyPlayedSongs.getRecentlyPlayedSongs();
	}

	public List<Song> getFrequentlyPlayedSongs() {
	    return frequentlyPlayedSongs.getFrequentlyPlayedSongs();
	}
	
    /**
     * Implement functionality to simulate the user playing a song
     */
	public void playSong(String title, String artist) {
		for (Song s : songs) {
			if ((s.getTitle()).equals(title) && (s.getArtist()).equals(artist)) {
				s.play();
				recentlyPlayedSongs.addSongRecent(s);
				frequentlyPlayedSongs.addSongFrequent(s);
			}
		}
	}
	
	
	public List<Song> getSongsSortedByTitle() {
		return getSortedList("title");
	}
	
	public List<Song> getSongsSortedByArtist() {
		return getSortedList("artist");
	}
	
	/*
	 * helper method for getting lists sorted by song title and artist
	 * */
	private List<Song> getSortedList(String command) {
		Map<Song, String> tempMap = new HashMap<>();
		List<Song> result = new ArrayList<>();
		for (Song s : songs) {
			if (command.equals("title")) tempMap.put(s, s.getTitle());
			else tempMap.put(s, s.getArtist());
		}
		// creates a list of entries of tempMap
		List<Map.Entry<Song, String>> entryList = new ArrayList<>(tempMap.entrySet());
		// sort by value (title/artist name)
		entryList.sort(Map.Entry.comparingByValue());
		for (Map.Entry<Song, String> e : entryList) {
			result.add(e.getKey());
		}
		return result;
	}

	/*
	 * returns a list of songs that is sorted by ratings
	 * */
	public List<Song> getSongsSortedByRating() {
		List<Song> result = new ArrayList<>();
		List<Map.Entry<Song, Integer>> entryList = new ArrayList<>(ratedSongs.entrySet());
		entryList.sort(Map.Entry.comparingByValue());
		for (Map.Entry<Song, Integer> e : entryList) {
			result.add(e.getKey());
		}
		return result;
	}
	
	/*
	 * helper method for identifying if there are multiple name of the given artist
	 * */
	private boolean ifDuplicate(String artist) {
		int temp = 0;
		for (Song s: songs) {
			if (s.getArtist().equals(artist)) temp++;
		}
		if (temp == 1) return false;
		return true;
	}
	
	/*
	 * removes a song from the library
	 * */
	public void removeSong(Song song) {
			songs.remove(song);
			favoriteSongs.remove(song);
			for (PlayList p: playlists) {
				p.removeSong(song);
			}
			recentlyPlayedSongs.removeSong(song);
			frequentlyPlayedSongs.removeSong(song);
			// if there is only one instance of an artist name of the given song, remove the name from 
			// the artists list
			if (!ifDuplicate(song.getArtist())) artists.remove(song.getArtist());
	}
	
	/*
	 * removes an album from the library
	 * */
	public void removeAlbum(Album album) {
		albums.remove(album);
		for (Song s : album.getSongs()) {
			removeSong(s);
		}
	}
	
	/*
	 * returns a shuffled list of songs
	 * */
	public List<Song> getShuffledSongs() {
		List<Song> shuffledSongs = new ArrayList<>(songs);
		Collections.shuffle(shuffledSongs);
		return shuffledSongs;
	}
	
	/*
	 * returns a shuffled list of songs in the given play list
	 * */
	public List<Song> shuffleSongsInPlayList(PlayList playlist) {
		return playlist.getShuffledSongs();
	}
}
