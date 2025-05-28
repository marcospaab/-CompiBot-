package org.tebot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {

        if(args.length < 2){
            System.out.println("Argumentos BOTNAME y TOKEN obligatorios");
            return;
        }
        String name = args[0];
        String token = args[1];

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot(token, name));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
