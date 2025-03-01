package main;

import model.LibraryModel;
import model.MusicStore;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        URL albumsTxtUrl = Main.class.getClassLoader().getResource("albums.txt");
        URL albumsDirectoryUrl = Main.class.getClassLoader().getResource("albums");

        try {
            Path albumsTxtPath = Paths.get(albumsTxtUrl.toURI());
            Path albumsDirectoryPath = Paths.get(albumsDirectoryUrl.toURI());

            MusicStore store = new MusicStore(albumsTxtPath.toString(), albumsDirectoryPath.toString());
            LibraryModel library = new LibraryModel();
            View view = new View(library, store);
            view.run();
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error loading album files: " + e.getMessage());
        }
    }
}