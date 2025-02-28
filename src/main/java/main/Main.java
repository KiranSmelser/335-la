package main;

import model.LibraryModel;
import model.MusicStore;
import view.View;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String albumsTxtPath = "src/main/resources/albums.txt";
        String albumsDirectoryPath = "src/main/resources/albums";

        try {
            MusicStore store = new MusicStore(albumsTxtPath, albumsDirectoryPath);
            LibraryModel library = new LibraryModel();
            View view = new View(library, store);
            view.run();
        } catch (IOException e) {
            System.err.println("Error loading album files: " + e.getMessage());
        }
    }
}