package ru.otus.processing.ms.message;

public class Message {
    private final String from;
    private final String to;
    private final String type;
    private final String message;

    public Message(String from, String to, String type, String message) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return String.format("{Message from = %s; to = %s; type = %s; message = %s", from, to, type, message);
    }
}
