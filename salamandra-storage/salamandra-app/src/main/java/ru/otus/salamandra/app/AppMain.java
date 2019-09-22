package ru.otus.salamandra.app;

import ru.otus.salamandra.app.sync.SyncService;

import java.util.Scanner;

public class AppMain {

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Salamandra!");
        System.out.println("Enter \"start\" to synchronize with server:");

        Scanner scanner = new Scanner(System.in);
        String inputLine;
        while(!(inputLine = scanner.nextLine()).equals("start")) {
            System.out.println("Illegal input!");
            System.out.println("Enter \"start\" to synchronize with server:");
        }

        System.out.println("Synchronization is starting");
        new SyncService().startSync();
        System.out.println("Synchronization has been started");

        while(true){}
    }

}
