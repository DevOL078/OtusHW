package ru.otus.processing.ms.socket;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.processing.ms.message.Message;
import ru.otus.processing.ms.message.MessageWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketMessageWorker implements MessageWorker {
    private static final int WORKER_COUNT = 2;
    private final Logger logger = LoggerFactory.getLogger(SocketMessageWorker.class);

    private final ExecutorService executorService;
    private Socket socket;

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    public SocketMessageWorker(Socket socket) {
        this.socket = socket;
        this.executorService = Executors.newFixedThreadPool(WORKER_COUNT);
    }

    public void init() {
        executorService.execute(this::sendBySocket);
        executorService.execute(this::receiveFromSocket);
    }

    @Override
    public Message poll() {
        return null;
    }

    @Override
    public void send(Message message) {
        output.add(message);
    }

    @Override
    public Message take() throws InterruptedException {
        return input.take();
    }

    @Override
    public void close() throws IOException {
        socket.close();
        executorService.shutdown();
    }

    private void sendBySocket() {
        try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while(socket.isConnected()) {
                Message message = output.take();
                String json = new Gson().toJson(message);
                logger.info("Sending message via socket to " + message.getTo());
                out.println(json);
                out.println();  //signal for end of message
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void receiveFromSocket() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
                if(inputLine.isEmpty()) {       //if this is the end of message
                    String json = stringBuilder.toString();
                    Message message = new Gson().fromJson(json, Message.class);
                    logger.info("Receive message via socket from " + message.getFrom());
                    input.add(message);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
