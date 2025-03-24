package model;

/**
 * Represents a Song using its title, artist, and album
 */
public class Song {
	private final String title;
    private final String artist;
    private final String albumTitle;
    private int playCount;

    /**
     * @pre title != null && artist != null && albumTitle != null
     */
    public Song(String title, String artist, String albumTitle) {
        this.title = title;
        this.artist = artist;
        this.albumTitle = albumTitle;
        this.playCount = 0;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }
    
    public void play() {
    	playCount ++;
    }
    
    public int getPlayCount() {
    	return playCount;
    }
}
