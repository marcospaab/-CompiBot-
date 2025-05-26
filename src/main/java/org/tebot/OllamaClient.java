package org.tebot;

import okhttp3.*;
import com.google.gson.*;

import java.io.IOException;
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
        String json = "{\"model\":\"mistral\", \"prompt\":\"" + prompt + "\", \"stream\":false}";

        Request request = new Request.Builder()
                .url("http://localhost:11434/api/generate")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();

            // Parsear JSON y extraer "response"
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            String botResponse = jsonObject.get("response").getAsString();

            // Aquí puedes modificar el texto si quieres que siempre incluya "CompiBot"
            return botResponse;
        }
    }
}
