package ru.otus.processing.ms;

import ru.otus.processing.ms.runner.ProcessRunnerImpl;
import ru.otus.processing.ms.socket.SocketServer;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageSystemMain {

    private final String FRONTEND1_START_COMMAND = "java -Dserver.port=8081 -jar hw16-multiprocessing\\hw16-frontend\\target\\hw16-frontend-2019-03-SNAPSHOT.jar Frontend1";
    private final int FRONTEND1_START_DELAY_SEC = 15;
    private final String FRONTEND2_START_COMMAND = "java -Dserver.port=8082 -jar hw16-multiprocessing\\hw16-frontend\\target\\hw16-frontend-2019-03-SNAPSHOT.jar Frontend2";
    private final int FRONTEND2_START_DELAY_SEC = 15;

    private final String DB1_START_COMMAND = "java -jar hw16-multiprocessing\\hw16-db\\target\\hw16-db-2019-03-SNAPSHOT-jar-with-dependencies.jar DB1";
    private final int DB1_START_DELAY_SEC = 5;
    private final String DB2_START_COMMAND = "java -jar hw16-multiprocessing\\hw16-db\\target\\hw16-db-2019-03-SNAPSHOT-jar-with-dependencies.jar DB2";
    private final int DB2_START_DELAY_SEC = 5;

    public static void main(String[] args) throws IOException, InterruptedException {
        new MessageSystemMain().start();
    }

    private void start() throws IOException, InterruptedException {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        startService(executorService, FRONTEND1_START_COMMAND, FRONTEND1_START_DELAY_SEC);
        startService(executorService, FRONTEND2_START_COMMAND, FRONTEND2_START_DELAY_SEC);
        startService(executorService, DB1_START_COMMAND, DB1_START_DELAY_SEC);
        startService(executorService, DB2_START_COMMAND, DB2_START_DELAY_SEC);

        SocketServer socketServer = new SocketServer();
        socketServer.start();
    }

    private void startService(ScheduledExecutorService executorService, String startCommand, int delaySec) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(startCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, delaySec, TimeUnit.SECONDS);
    }

}
