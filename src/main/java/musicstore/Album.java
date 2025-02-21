package musicstore;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Album using its title, artist, genre, year, and list of songs
 */
public class Album {
    private final String title;
    private final String artist;
    private final String genre;
    private final int year;

    private final List<Song> songs;

    /**
     * @pre title != null && artist != null && genre != null && year != null && songs != null
     */
    public Album(String title, String artist, String genre, int year, List<Song> songs) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.year = year;
        this.songs = new ArrayList<>(songs);	// copies ArrayList to prevent escaping reference
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<Song> getSongs() {
        return new ArrayList<>(songs);	// copies ArrayList to prevent escaping reference
    }
}
