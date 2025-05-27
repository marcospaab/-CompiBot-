package org.tebot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StickerRepository {

    private static final String FILE_PATH = "stickers.json";
    private static final List<String> stickerFileIds = new ArrayList<>();
    private static final Random random = new Random();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        loadStickersFromFile();
    }

    public static String getRandomStickerFileId() {
        return stickerFileIds.get(random.nextInt(stickerFileIds.size()));
    }

    public static void addStickerFileId(String fileId) {
        if (!stickerFileIds.contains(fileId)) {
            stickerFileIds.add(fileId);
            saveStickersToFile();
            System.out.println("Añadido y guardado nuevo sticker: " + fileId);
        }
    }

    public static List<String> getAllStickerFileIds() {
        return new ArrayList<>(stickerFileIds);
    }

    private static void saveStickersToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), stickerFileIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadStickersFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                List<String> loadedStickers = objectMapper.readValue(file, new TypeReference<List<String>>() {});
                stickerFileIds.addAll(loadedStickers);
                System.out.println("Stickers cargados desde archivo: " + stickerFileIds.size());
            } else {
                System.out.println("Archivo de stickers no encontrado. Se crea uno nuevo al añadir.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
