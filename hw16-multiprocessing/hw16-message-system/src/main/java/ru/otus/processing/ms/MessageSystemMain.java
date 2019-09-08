package ru.otus.processing.ms;

import ru.otus.processing.ms.runner.ProcessRunnerImpl;
import ru.otus.processing.ms.socket.SocketServer;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageSystemMain {

    private final String FRONTEND_START_COMMAND = "java -jar hw16-multiprocessing\\hw16-frontend\\target\\hw16-frontend-2019-03-SNAPSHOT.jar";
    private final int FRONTEND_START_DELAY_SEC = 5;
    private final String DB_START_COMMAND = "java -jar hw16-multiprocessing\\hw16-db\\target\\hw16-db-2019-03-SNAPSHOT-jar-with-dependencies.jar -port 8082";
    private final int DB_START_DELAY_SEC = 5;

    public static void main(String[] args) throws IOException, InterruptedException {
        new MessageSystemMain().start();
    }

    private void start() throws IOException, InterruptedException {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        startFrontend(executorService);
        startDB(executorService);

        SocketServer socketServer = new SocketServer();
        socketServer.start();
    }

    private void startFrontend(ScheduledExecutorService executorService) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(FRONTEND_START_COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, FRONTEND_START_DELAY_SEC, TimeUnit.SECONDS);
    }

    private void startDB(ScheduledExecutorService executorService) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(DB_START_COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, DB_START_DELAY_SEC, TimeUnit.SECONDS);
    }

}
