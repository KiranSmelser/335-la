package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryModel {

	private final List<Song> songs;
	private final List<String> artists;
	private final List<Album> albums;
	private final List<Song> favoriteSongs;
	private final List<PlayList> playlists;
	private final Map<Song, Integer> ratedSongs;

	public LibraryModel() {
		songs = new ArrayList<>();
		artists = new ArrayList<>();
		albums = new ArrayList<>();
		favoriteSongs = new ArrayList<>();
		playlists = new ArrayList<>();
		ratedSongs = new HashMap<>();
	}

	public void addSong(Song song) {
		songs.add(song);
		artists.add(song.getArtist());
	}

	public void addAlbum(Album album) {
		albums.add(album);
		for (Song song : album.getSongs()) {
			songs.add(song);
		}
	}

	public Song getSongByTitle(String title) {
		for (Song song : songs) {
			if (song.getTitle().equals(title)) return song;
		}
		return null;
	}

	public Song getSongByArtist(String artist) {
		for (Song song : songs) {
			if (song.getArtist().equals(artist)) return song;
		}
		return null;
	}

	public Album getAlbumByTitle(String title) {
		for (Album album : albums) {
			if (album.getTitle().equals(title)) return album;
		}
		return null;
	}

	public Album getAlbumByArtist(String title) {
		for (Album album : albums) {
			if (album.getArtist().equals(title)) return album;
		}
		return null;
	}
	
	public List<String> getSongTitles() {
		List<String> songTitles = new ArrayList<>();
		for (Song song : songs) {
			songTitles.add(song.getTitle());
		}
		return songTitles;
	}
	
	public List<String> getArtists() {
		return Collections.unmodifiableList(artists);
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
	
	public boolean rateSong(Song song, int rate) {
		if (rate >= 6 || rate <= -1) return false;
		if (!songs.contains(song)) return false;
		ratedSongs.put(song, rate);
		if (rate == 5) favoriteSongs.add(song);
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
	
}
