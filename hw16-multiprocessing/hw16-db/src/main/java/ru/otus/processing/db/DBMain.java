package ru.otus.processing.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.db.socket.DBSocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

public class DBMain {

    private static Logger logger = LoggerFactory.getLogger(DBMain.class);

    private static final String MS_HOST = "localhost";
    private static final int MS_PORT = 8080;

    public static void main(String[] args) throws IOException {
        logger.info("Hello from DB");
        DBSocketMessageWorker dbSocketMessageWorker = new DBSocketMessageWorker(new Socket(MS_HOST, MS_PORT));
        dbSocketMessageWorker.init();
    }


}
