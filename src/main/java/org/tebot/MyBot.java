package org.tebot;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import javax.sound.sampled.*;
import java.io.File;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {
    private static final String VOICE_NAME = "cmu-slt-hsmm";
    private final String botUsername;
    private final OllamaClient ollamaClient;
    private final MaryInterface marytts;

    public MyBot(String token, String botUsername) {
        super(token);
        this.botUsername = botUsername;
        this.ollamaClient = new OllamaClient();
        try {
            marytts = new LocalMaryInterface();
            marytts.setVoice(VOICE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando MaryTTS", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;

        if (update.getMessage().hasSticker()) {
            StickerRepository.addStickerFileId(update.getMessage().getSticker().getFileId());
        }

        if (update.getMessage().hasText()) {
            handleIncomingText(update.getMessage());
        }
    }

    private void handleIncomingText(Message message) {
        long chatId = message.getChatId();
        String text = message.getText();
        String chatIdStr = String.valueOf(chatId);  // <-- convertir aquí a String

        new Thread(() -> {
            try {
                switch (text.toLowerCase()) {
                    case "/sticker" -> sendRandomSticker(chatId);
                    case "/voz" -> sendVoiceMessage(chatId, "¡Hola! Soy CompiBot y ahora puedo hablar.");
                    default -> {
                        ConversationHistory.addMessage(chatIdStr, "Usuario: " + text);
                        String context = buildConversationContext(chatIdStr);
                        String aiResponse = ollamaClient.getChatResponse(context, text);
                        ConversationHistory.addMessage(chatIdStr, "CompiBot: " + aiResponse);
                        sendVoiceMessage(chatId, aiResponse);
                        sendTextMessage(chatId, "CompiBot: " + aiResponse);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendTextMessage(chatId, "Lo siento, hubo un error al procesar tu solicitud.");
            }
        }).start();
    }

    private void sendRandomSticker(long chatId) {
        try {
            String stickerId = StickerRepository.getRandomStickerFileId();
            execute(SendSticker.builder()
                    .chatId(chatId)
                    .sticker(new InputFile(stickerId))
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            sendTextMessage(chatId, "No pude enviarte el sticker, pringao.");
        }
    }

    private void sendTextMessage(long chatId, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendVoiceMessage(long chatId, String text) {
        try {
            File audioFile = new File("output_" + System.currentTimeMillis() + ".wav");
            AudioInputStream audio = marytts.generateAudio(text);
            AudioSystem.write(audio, AudioFileFormat.Type.WAVE, audioFile);

            execute(SendAudio.builder()
                    .chatId(chatId)
                    .audio(new InputFile(audioFile))
                    .build());

            audioFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildConversationContext(String chatId) {
        List<MessageEntry> history = ConversationHistory.getHistory(chatId);
        StringBuilder contextBuilder = new StringBuilder();
        for (MessageEntry entry : history) {
            contextBuilder.append(entry.getTimestamp())
                    .append(" - ")
                    .append(entry.getMessage())
                    .append("\n");
        }
        return contextBuilder.toString();
    }
}
