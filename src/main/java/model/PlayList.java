package model;
import java.util.List;
import java.util.ArrayList;


public class PlayList {
	private final String name;
	private final List<Song> songs;
	
	public PlayList(String name) {
		this.name = name;
		this.songs = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addSong(Song song) {
		songs.add(song);
	}
	
	public void removeSong(Song song) {
		songs.remove(song);
	}
	
    public List<Song> getSongs() {
    	return new ArrayList<>(songs);	// copies ArrayList to prevent escaping reference
    }
}
