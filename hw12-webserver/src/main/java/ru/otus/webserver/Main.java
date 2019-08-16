package ru.otus.webserver;

import org.eclipse.jetty.server.Server;
import ru.otus.webserver.creator.ServerProcessing;

public class Main {

    private static int PORT = 8081;

    public static void main(String[] args) throws Exception {
        Server server = ServerProcessing.createServer(PORT);
        server.start();
        server.join();
    }

}
