package org.tebot;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConversationHistory {
    private static final String HISTORY_DIR = "history";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Map que guarda la lista de mensajes por usuario (clave: chatId String)
    private static Map<String, List<MessageEntry>> conversations = new HashMap<>();

    static {
        loadHistories();
    }

    // Añade un mensaje al historial del usuario y lo guarda
    public static synchronized void addMessage(String chatId, String message) {
        List<MessageEntry> history = conversations.computeIfAbsent(chatId, k -> new ArrayList<>());
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        history.add(new MessageEntry(timestamp, message));
        saveHistory(chatId);
    }

    // Devuelve la lista de mensajes guardados para un usuario
    public static synchronized List<MessageEntry> getHistory(String chatId) {
        return conversations.getOrDefault(chatId, new ArrayList<>());
    }

    // Carga los historiales desde archivos JSON
    private static void loadHistories() {
        try {
            Files.createDirectories(Paths.get(HISTORY_DIR));
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(HISTORY_DIR), "*.json");
            for (Path file : stream) {
                String chatId = file.getFileName().toString().replace(".json", "");
                String content = new String(Files.readAllBytes(file));
                Type listType = new TypeToken<List<MessageEntry>>(){}.getType();
                List<MessageEntry> history = gson.fromJson(content, listType);
                conversations.put(chatId, history);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Guarda el historial de un usuario en JSON
    private static void saveHistory(String chatId) {
        try {
            Files.createDirectories(Paths.get(HISTORY_DIR));
            String json = gson.toJson(conversations.get(chatId));
            Files.write(Paths.get(HISTORY_DIR, chatId + ".json"), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
