package com.bndev.ood.hittastic.console;

import com.bndev.ood.hittastic.api.Session;
import com.bndev.ood.hittastic.api.Song;
import com.bndev.ood.hittastic.api.User;
import de.vandermeer.asciitable.AsciiTable;

import javax.naming.AuthenticationException;
import javax.security.auth.login.FailedLoginException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class App {
    protected Session session;
    protected Song selectedSong;

    protected User selectedUser;

    public Reader inputReader = new InputStreamReader(System.in);

    public ConsoleMenu loginMenu = new ConsoleMenu("Login Menu", "Please enter a selection: ", true);
    public ConsoleMenu mainMenu = new ConsoleMenu("Main Menu", "Please enter a selection: ", "Logout", "Logging out...", () -> {
        this.session = null;
        this.selectedUser = null;
        this.selectedSong = null;
    });
    public ConsoleMenu searchMenu = new ConsoleMenu("Song Search", "Please enter a selection: ");
    public ConsoleMenu songsMenuAdmin = new ConsoleMenu("(Admin) Song Management", "Please enter a selection: ");
    public ConsoleMenu usersMenuAdmin = new ConsoleMenu("(Admin) User Management", "Please enter a selection: ");


    public App() {
        Song.reset();
        User.Reset();
        Song.Load();
        User.Load();

        loginMenu.addAction("Login", () -> {
            // Login loop
            while (true) {
                Session checkSession = null;
                Scanner scanner = new Scanner(System.in);
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                try {
                    checkSession = User.Login(username, password);
                } catch (FailedLoginException e) {
                    System.out.println("Invalid login details, please try again\n");
                }
                if (checkSession != null) {
                    this.session = checkSession;
                    break;
                }
            }
            populateMainMenu();
        });


        populateSearchMenu();
        populateAdminSongsMenu();
        populateAdminUsersMenu();
    }

    public void populateMainMenu() {
        mainMenu.addAction("Song Search", searchMenu);
        mainMenu.addAction("Buy Selected Song", () -> {
            if (this.selectedSong == null) {
                ConsoleMenu.waitForReturn("Please use 'Song Search' to select a song before buying");

            } else {
                while (true) {
                    Scanner scanner = new Scanner(this.inputReader);
                    System.out.println("Currently selected song: '" + this.selectedSong.title + "'");
                    System.out.print("Number of copies to order: ");
                    if (scanner.hasNextInt()) {
                        int copies = scanner.nextInt();
                        this.session.BuySong(selectedSong, copies);
                        System.out.println("Song purchased successfully");
                        break;
                    }
                    System.out.println("Invalid entry, please enter an integer\n");
                }
            }

        });
        mainMenu.addAction("View Order History", () -> {
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Date", "Time", "Artist", "Title", "Qty");
            session.user.orders.forEach(order -> {
                at.addRule();
                at.addRow(order.timeOfOrder.toLocalDate(), order.timeOfOrder.format(DateTimeFormatter.ofPattern("HH:mm:ss")), order.song.artist, order.song.title, order.qty);
            });
            at.addRule();
            System.out.println(at.render());
        });

        // admin only actions added if user is an admin
        if (session.user.isAdmin) {
            mainMenu.addAction("(Admin) Songs", songsMenuAdmin);
            mainMenu.addAction("(Admin) Users", usersMenuAdmin);
        }
        System.out.println("Login Successful!");
        mainMenu.run();
    }


    public void populateSearchMenu() {
        searchMenu.addAction("Search By Title", () -> {
            ConsoleMenu searchResults = new ConsoleMenu("Search Results", "Please enter a song to select: ");
            Scanner scanner = new Scanner(this.inputReader);
            System.out.print("Search (Title): ");
            String search = scanner.nextLine();
            List<Song> songs = session.FindByTitle(search);
            songDisplayer(searchResults, songs);
        });
        searchMenu.addAction("Search By Artist", () -> {
            ConsoleMenu searchResults = new ConsoleMenu("Search Results", "Please enter a song to select: ");
            Scanner scanner = new Scanner(this.inputReader);

            System.out.print("Search (Artist): ");
            String search = scanner.nextLine();
            List<Song> songs = session.FindByArtist(search);
            songDisplayer(searchResults, songs);
        });
    }

    private void songDisplayer(ConsoleMenu searchResults, List<Song> songs) {
        if (songs.size() == 0) {
            ConsoleMenu.waitForReturn("No matching songs found");
        } else {
            songs.forEach(song -> {
                searchResults.addAction(song.title + " - " + song.artist, () -> {
                    this.selectedSong = song;
                    System.out.println("\n" + song.title + " has been selected");
                    searchResults.stopMenu();
                });
            });
        }
        searchResults.run();
    }

    public void populateAdminSongsMenu() {
        songsMenuAdmin.addAction("Add new song", () -> {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Song Title: ");
            String songTitle = scanner.nextLine();

            System.out.print("Song Artist: ");
            String songArtist = scanner.nextLine();

            try {
                Song newSong = this.session.AddSong(songTitle, songArtist);
                System.out.println("New song added with ID: " + newSong.id());
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void populateAdminUsersMenu() {
        usersMenuAdmin.addAction("View All Users", () -> {
            try {
                List<User> users = this.session.GetAllUsers();
                ConsoleMenu allUsers = new ConsoleMenu("All Users", "Please select a user: ");
                users.forEach(user -> {
                    allUsers.addAction(user.username + " - " + (user.isAdmin ? "admin" : "user"), () -> {
                        this.selectedUser = user;
                        System.out.println("\n" + user.username + " has been selected");
                        allUsers.stopMenu();
                    });
                });
                allUsers.run();
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
        });

        usersMenuAdmin.addAction("Select user by username", () -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Username: ");
                String username = scanner.nextLine();
                try {
                    User user = this.session.GetUserByUsername(username);

                    if (user != null) {
                        this.selectedUser = user;
                        break;
                    }
                    System.out.println("No user exists with the entered username");
                    System.out.print("Try again ? (y/n)");
                    String tryAgain = scanner.next();
                    if (!Objects.equals(tryAgain, "y")) {
                        break;
                    }
                } catch (AuthenticationException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        usersMenuAdmin.addAction("Edit selected user", () -> {
            if (this.selectedUser == null) {
                ConsoleMenu.waitForReturn("Please select a user using option 1 or 2 before using this");
            } else {
                userCreator(true);
            }
        });

        usersMenuAdmin.addAction("Delete selected user", () -> {
            if (this.selectedUser == null) {
                ConsoleMenu.waitForReturn("Please select a user using option 1 or 2 before using this");
            } else {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.print("Do you want to delete user '" + this.selectedUser.username + "' (y/n)");
                    String confirm = scanner.next();
                    if (confirm.equals("y")) {
                        try {
                            this.session.DeleteUser(this.selectedUser.username);
                        } catch (AuthenticationException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (confirm.equals("n")) {
                        
                    }
                    System.out.println("Please enter either 'y' or 'n'");
                }

            }
        });

        usersMenuAdmin.addAction("Create new user", () -> {
            userCreator(false);
        });
    }

    private void userCreator(boolean edit) {
        Scanner scanner = new Scanner(System.in);
        String newUsername;
        String newPassword;
        boolean isAdmin;
        while (true) {
            System.out.print("New Username: ");
            newUsername = scanner.nextLine();

            boolean exists = User.all.containsKey(newUsername);

            if (!exists) break;

            if (edit && newUsername.equals(this.selectedUser.username)) {
                break;
            }

            System.out.println("This username already exists, please try again");
        }
        while (true) {
            System.out.print("New Password: ");
            newPassword = scanner.nextLine();
            if (newPassword.isEmpty()) {
                System.out.println("Invalid entry, please try again");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Admin Privileges (y/n): ");
            String adminString = scanner.next();
            if (adminString.equals("y")) {
                isAdmin = true;
                break;
            } else if (adminString.equals("n")) {
                isAdmin = false;
                break;
            } else {
                System.out.println("Please enter either 'y' or 'n'");
            }
        }
        if (edit) {
            this.selectedUser.Update(newUsername, newPassword, isAdmin);
        } else {
            try {
                this.session.CreateUser(newUsername, newPassword, isAdmin);
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public void run() {
        loginMenu.run();
    }
}
