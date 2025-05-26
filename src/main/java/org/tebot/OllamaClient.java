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
        // Modificamos el prompt para incluir personalidad sarcástica
        String sarcasticPrefix = "Eres CompiBot, un asistente con un tono sarcástico e irónico creado por Marcos Padín. " +
                "Respondes de forma agresiva riendote de los prompts que te envía la gente.";

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
            // Definir la ruta del archivo dentro del proyecto
            String directoryPath = "logs";  // Carpeta dentro del proyecto
            String filePath = directoryPath + "/prompts.txt";

            // Crear la carpeta si no existe
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Escribir el prompt en el archivo
            try (FileWriter fileWriter = new FileWriter(filePath, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.println(prompt);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
