package ru.otus.processing.ms.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.message.MessageWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    private static final int PORT = 8080;   //TODO придумать, как вынести в конфиг
    private static final int THREADS_COUNT = 4;
    private final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private final ExecutorService executorService;
    private final Map<String, MessageWorker> workers;

    public SocketServer() {
        logger.info("Start socket server on port " + PORT);
        executorService = Executors.newFixedThreadPool(THREADS_COUNT);
        workers = new ConcurrentHashMap<>();
    }

    public void start() throws IOException, InterruptedException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!executorService.isShutdown()) {
                logger.info("Wait for new socket");
                Socket socket = serverSocket.accept();
                logger.info("Catch new socket");
                addWorker(socket);
            }
        }
    }

    private void routeMessages(SocketMessageWorker worker) throws InterruptedException {
        while(!executorService.isShutdown()) {
            Message message = worker.take();
            logger.info("Receive new message from " + message.getFrom() + " to " + message.getTo());
            MessageWorker workerTo = workers.get(message.getTo());
            workerTo.send(message);
            logger.info("Message has been sent to " + message.getTo());
        }
    }

    private void addWorker(Socket socket) throws InterruptedException {
        SocketMessageWorker worker = new SocketMessageWorker(socket);
        worker.init();
        logger.info("SocketMessageWorker has been init");
        String serviceId = getRemoteServiceId(worker);
        logger.info("New service: " + serviceId);
        workers.put(serviceId, worker);
        executorService.submit(() -> {
            try {
                routeMessages(worker);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
    }

    private String getRemoteServiceId(SocketMessageWorker worker) throws InterruptedException {
        Message message = worker.take();
        if(!message.getType().equals("service-id")) {
            throw new IllegalStateException("Expected message with type service-id, but actual is " + message.getType());
        }
        return message.getFrom();
    }
}
