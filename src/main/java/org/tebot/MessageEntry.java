package org.tebot;

public class MessageEntry {
    private String timestamp;
    private String message;

    public MessageEntry(String timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}

