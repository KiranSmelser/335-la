package musicstore;
import java.util.Collections;
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
	
    public List<Song> getSongs() {
        return Collections.unmodifiableList(songs);
    }
}
