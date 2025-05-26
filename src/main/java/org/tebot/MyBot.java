package org.tebot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chat_id = update.getMessage().getChatId();
            String message_received = update.getMessage().getText();

            new Thread(() -> {
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


    @Override
    public String getBotUsername() {
        return this.botUsername;
    }
}
