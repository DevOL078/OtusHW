package ru.otus.salamandra.app;

import ru.otus.salamandra.app.route.RouteService;
import ru.otus.salamandra.app.sync.SyncJob;

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

        System.out.println("Updating route is starting");
        new RouteService().startRoute();
        System.out.println("Updating route has been started");

        new SyncJob().startJob();
        System.out.println("Start sync job");

        while(true){}
    }

}
