package ru.otus.salamandra.app;

import ru.otus.salamandra.app.route.RouteService;
import ru.otus.salamandra.app.service.AuthService;
import ru.otus.salamandra.app.store.SessionStore;
import ru.otus.salamandra.app.sync.SyncJob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

public class AppMain {

    private static String configName;
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        if(args.length > 0) {
            configName = args[0];
        }

        System.out.println("Welcome to Salamandra!");

        login();

        System.out.println("Successful authorization as " + SessionStore.getInstance().getUserLogin() + "!");

        System.out.println("Enter \"start\" to synchronize with server:");

        String inputLine;
        while(!(inputLine = reader.readLine()).equals("start")) {
            System.out.println("Illegal input!");
            System.out.println("Enter \"start\" to synchronize with server:");
        }

        System.out.println("Updating route is starting");
        new RouteService().startRoute();
        System.out.println("Updating route has been started");

        Thread.sleep(5000);

        new SyncJob().startJob();
        System.out.println("Start sync job");

//        while(true){}
    }

    private static void login() {
        System.out.println("Let's login!");
        while(true) {
            System.out.println("1 - sign in");
            System.out.println("2 - register new user");
            try {
                int choice = Integer.parseInt(reader.readLine());
                switch (choice) {
                    case 1: {
                        System.out.println("Login: ");
                        String login = reader.readLine();
                        System.out.println("Password: ");
                        String password = reader.readLine();
                        System.out.println("Base directory path: ");
                        String baseDir = reader.readLine();
                        boolean exists = Files.exists(Paths.get(baseDir));
                        if(!exists) {
                            throw new IllegalStateException("Cannot find base directory");
                        }
                        System.out.println("Authorization...");
                        AuthService.getInstance().signIn(login, password, baseDir);
                        break;
                    }
                    case 2: {
                        System.out.println("Login: ");
                        String login = reader.readLine();
                        System.out.println("Password: ");
                        String password = reader.readLine();
                        System.out.println("Base directory path: ");
                        String baseDir = reader.readLine();
                        boolean exists = Files.exists(Paths.get(baseDir));
                        if(!exists) {
                            throw new IllegalStateException("Cannot find base directory");
                        }
                        System.out.println("Authorization...");
                        AuthService.getInstance().register(login, password, baseDir);
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Illegal input!");
                    }
                }
                break;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static Optional<String> getConfigName() {
        return Optional.ofNullable(configName);
    }

}
