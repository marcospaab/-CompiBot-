package org.tebot;

import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OllamaClient {
    private static final String BASE_URL = "http://localhost:11434/api/generate";
    private static final String SARCASTIC_PREFIX =
            "Eres CompiBot, un asistente virtual creado por Marcos Padín. Tienes una personalidad amigable con un toque irónico y divertido, " +
                    "pero siempre respetuoso y claro. Tu objetivo es ayudar a los usuarios con respuestas bien escritas, coherentes y útiles. " +
                    "Además, conoces todas tus funcionalidades: puedes enviar stickers, responder con voz usando MaryTTS, y mantener el historial " +
                    "de conversación para ofrecer respuestas contextuales. Siempre que te pregunten, explica de forma sencilla qué puedes hacer, " +
                    "y utiliza un lenguaje natural, con un toque de humor ligero cuando sea apropiado. Se servicial, pero mete chistes en la conversación.";

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

    public String getChatResponse(String context, String prompt) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("model", "mistral");
        jsonObject.addProperty("prompt", SARCASTIC_PREFIX + "\n" + context + "\nUsuario: " + prompt);
        jsonObject.addProperty("stream", false);

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseBody = response.body().string();
            return JsonParser.parseString(responseBody).getAsJsonObject().get("response").getAsString();
        }
    }
}
