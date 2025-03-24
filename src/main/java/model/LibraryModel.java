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

	public LibraryModel() {
		songs = new ArrayList<>();
		artists = new HashSet<>();
		albums = new ArrayList<>();
		favoriteSongs = new ArrayList<>();
		playlists = new ArrayList<>();
		ratedSongs = new HashMap<>();
		recentlyPlayedSongs = new PlayList("Most Recently Played Songs");
		frequentlyPlayedSongs = new PlayList("Most Frequently Played Songs");
	}
	
	/**
     * Adds a song to library if not already present
     */
	public void addSong(Song song) {
		if (!songs.contains(song)) {
            songs.add(song);
            artists.add(song.getArtist());
        }
	}
	
	/**
     * Adds an album and all its songs to library if not already present
     */
	public void addAlbum(Album album) {
		if (!albums.contains(album)) {
            albums.add(album);
        }
        for (Song song : album.getSongs()) {
            if (!songs.contains(song)) {
                songs.add(song);
                artists.add(song.getArtist());
            }
        }
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
