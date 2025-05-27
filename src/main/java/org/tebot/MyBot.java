package org.tebot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final OllamaClient ollamaClient;

    public MyBot(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
        this.ollamaClient = new OllamaClient();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleIncomingSticker(update);

            if (update.getMessage().hasText()) {
                long chat_id = update.getMessage().getChatId();
                String message_received = update.getMessage().getText();

                new Thread(() -> {
                    if (message_received.equalsIgnoreCase("/sticker")) {
                        enviarStickerAleatorio(chat_id);
                        return;
                    }

                    try {
                        String aiResponse = ollamaClient.getChatResponse(message_received);
                        String responseWithName = "CompiBot: " + aiResponse;

                        SendMessage message = SendMessage.builder()
                                .chatId(chat_id)
                                .text(responseWithName)
                                .build();
                        execute(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            SendMessage errorMessage = SendMessage.builder()
                                    .chatId(chat_id)
                                    .text("Lo siento, hubo un error al procesar tu solicitud.")
                                    .build();
                            execute(errorMessage);
                        } catch (TelegramApiException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    private void handleIncomingSticker(Update update) {
        if (update.hasMessage() && update.getMessage().hasSticker()) {
            String fileId = update.getMessage().getSticker().getFileId();
            StickerRepository.addStickerFileId(fileId);
        }
    }


    private void enviarStickerAleatorio(long chatId) {
        try {
            String randomStickerId = StickerRepository.getRandomStickerFileId();

            SendSticker sticker = new SendSticker();
            sticker.setChatId(chatId);
            sticker.setSticker(new InputFile(randomStickerId));

            execute(sticker);

        } catch (TelegramApiException e) {
            e.printStackTrace();
            try {
                SendMessage errorMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("No pude enviarte el sticker, pringao.")
                        .build();
                execute(errorMessage);
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }
}
