package ru.otus.processing.ms.message;

import ru.otus.processing.ms.message.Message;

import java.io.IOException;

public interface MessageWorker {
    Message poll();

    void send(Message message);

    Message take() throws InterruptedException;

    void close() throws IOException;
}
