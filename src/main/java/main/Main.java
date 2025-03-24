package main;

import model.LibraryModel;
import model.MusicStore;
import model.UserManager;
import view.View;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Main entry point for the music library application.
 */
public class Main {
    public static void main(String[] args) {
        String albumsPath = "./src/main/resources/albums.txt";
        String albumsDirectory = "./src/main/resources/albums";

        try {
            MusicStore store = new MusicStore(albumsPath, albumsDirectory);

            String userJsonPath = "/src/main/resources/users.json";
            UserManager userManager = new UserManager(userJsonPath, store);

            LibraryModel library;

            Scanner sc = new Scanner(System.in);
            boolean done = false;

            while (!done) {
                System.out.println("=======================================");
                System.out.println("        Welcome to Music Library");
                System.out.println("=======================================");
                System.out.println("1) Log in");
                System.out.println("2) Create New User");
                System.out.println("3) Quit");
                System.out.print("Select an option: ");
                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1": {
                        System.out.print("Enter username: ");
                        String username = sc.nextLine().trim();
                        System.out.print("Enter password: ");
                        String password = sc.nextLine().trim();

                        boolean auth = userManager.authenticate(username, password);
                        if (!auth) {
                            System.out.println("Login failed. Check your credentials and try again.");
                        } else {
                            library = new LibraryModel();
                            userManager.loadUserLibrary(library);

                            View view = new View(library, store, userManager);
                            view.run();

                            try {
                                userManager.saveUserLibrary(library);
                            } catch (IOException e) {
                                System.out.println("Error saving library data: " + e.getMessage());
                            }

                            userManager.logout();
                        }
                        break;
                    }
                    case "2": {
                        System.out.print("Enter desired username: ");
                        String newUser = sc.nextLine().trim();
                        System.out.print("Enter desired password: ");
                        String newPass = sc.nextLine().trim();

                        try {
                            boolean created = userManager.createUser(newUser, newPass);
                            if (created) {
                                System.out.println("User created successfully! You can now log in.");
                            } else {
                                System.out.println("That username already exists. Please pick another.");
                            }
                        } catch (IOException e) {
                            System.out.println("Error creating user: " + e.getMessage());
                        }
                        break;
                    }
                    case "3": {
                        System.out.println("Goodbye!");
                        done = true;
                        break;
                    }
                    default:
                        System.out.println("Unrecognized option.");
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading album files or user data: " + e.getMessage());
        }
    }
}