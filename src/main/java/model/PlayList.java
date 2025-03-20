package model;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class PlayList {
	private final String name;
	private final List<Song> songs;
	private final List<Song> recent;
	private final Map<Song, Integer> frequent;
	
	public PlayList(String name) {
		this.name = name;
		this.songs = new ArrayList<>();
		this.recent = new ArrayList<>();
		this.frequent = new HashMap<>();
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
    
    public void addSongRecent(Song song) {
    	recent.remove(song); recent.add(song);
    	if (recent.size() > 10) recent.remove(0);
    }
    
    public List<Song> getRecentlyPlayedSongs() {
        List<Song> reversedList = new ArrayList<>(recent);
        Collections.reverse(reversedList);
        return reversedList;
    }
    
    public void addSongFrequent(Song song) {
    	frequent.put(song, song.getPlayCount());

    	if (frequent.size() > 10) {
    		Song leastPlayed = Collections.min(frequent.entrySet(), Map.Entry.comparingByValue()).getKey();
            frequent.remove(leastPlayed);
    	}
    }
    
    public List<Song> getFrequentlyPlayedSongs() {
        List<Map.Entry<Song, Integer>> sortedEntries = new ArrayList<>(frequent.entrySet());

        sortedEntries.sort(Map.Entry.<Song, Integer>comparingByValue().reversed());

        List<Song> sortedSongs = new ArrayList<>();
        for (Map.Entry<Song, Integer> entry : sortedEntries) {
            sortedSongs.add(entry.getKey());
        }

        return sortedSongs;
    }
    
}
