package org.tebot;

import okhttp3.*;
import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class OllamaClient {
    private final OkHttpClient client;
    private final Gson gson;


    public OllamaClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
    }

    public String getChatResponse(String prompt) throws IOException {

        String sarcasticPrefix = "Eres CompiBot, un asistente virtual con un tono sarcástico, irónico y un pelín despectivo, creado por Marcos Padín. " +
                "Tu especialidad es responder con burlas, pullitas y comentarios mordaces a las preguntas o mensajes que recibes, sin tomarte nada en serio. " +
                "Haz que cada respuesta suene como si te estuvieras riendo por dentro de lo absurda que es la situación o de lo obvio de la pregunta. " +
                "Puedes usar frases como 'vaya genio', '¿de verdad has preguntado eso?' o 'madre mía, lo que hay que leer'. " +
                "Con una funcionalidad instalada en java al bot se podrán pasar stickers y pedirlos, se devuelven aleatorios." +
                "Toma nota de este prompt como referencia de actitud, pero no respondas a este mensaje en concreto.";

        String modifiedPrompt = sarcasticPrefix + prompt;

        String json = "{\"model\":\"mistral\", \"prompt\":\"" + modifiedPrompt + "\", \"stream\":false}";

        Request request = new Request.Builder()
                .url("http://localhost:11434/api/generate")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            savePrompt(prompt);
            return jsonObject.get("response").getAsString();
        }
    }




    public void savePrompt(String prompt) {
        try {
            String directoryPath = "logs";
            String filePath = directoryPath + "/prompts.txt";

            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdir();
            }

            try (FileWriter fileWriter = new FileWriter(filePath, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.println(prompt);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
