package com.yourpackage.coachdevie.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * AI integration. Public Android apps must call a secure backend, not a provider
 * directly with a secret key embedded in the APK.
 *
 * Configure HTTPS_BACKEND_URL below with the public HTTPS URL of a backend you
 * own. The backend should securely attach the provider key, authenticate users,
 * enforce rate limits, and return an OpenAI-compatible chat completion JSON.
 */
public final class ApiClient {
    // Example: "https://your-domain.example/api/chat" -- this URL is NOT a secret.
    private static final String HTTPS_BACKEND_URL = "";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private ApiClient() { }

    public static String generateCoaching(String prompt) throws Exception {
        if (HTTPS_BACKEND_URL.trim().isEmpty()) {
            throw new IOException("Assistant IA non configuré : ajoute une URL de backend HTTPS sécurisé.");
        }
        if (!HTTPS_BACKEND_URL.startsWith("https://")) {
            throw new IOException("Le backend IA doit utiliser HTTPS.");
        }

        JSONObject json = new JSONObject();
        json.put("model", "openai/gpt-4o");
        json.put("max_tokens", 400);
        json.put("temperature", 0.8);

        JSONArray messages = new JSONArray();
        JSONObject system = new JSONObject();
        system.put("role", "system");
        system.put("content", "Tu es un coach de vie bienveillant et émotionnellement intelligent. " +
                "Tes réponses doivent être chaleureuses, pratiques, élégantes et faciles à lire.");
        JSONObject user = new JSONObject();
        user.put("role", "user");
        user.put("content", prompt);
        messages.put(system);
        messages.put(user);
        json.put("messages", messages);

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(HTTPS_BACKEND_URL)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("AI backend returned HTTP " + response.code());
            }
            if (response.body() == null) {
                throw new IOException("AI backend returned no content");
            }
            JSONObject result = new JSONObject(response.body().string());
            JSONObject message = result.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message");
            return message.getString("content");
        }
    }
}
