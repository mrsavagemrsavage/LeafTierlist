package com.leaftiertagger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TierApiClient {
    private static final Gson GSON = new Gson();
    private static final String API_BASE_URL = LeafTierTagger.API_URL;

    public static TierData fetchPlayerTier(String username) {
        try {
            URL url = new URL(API_BASE_URL + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonObject json = GSON.fromJson(response.toString(), JsonObject.class);
                return new TierData(
                        json.get("minecraft_username").getAsString(),
                        json.get("tier").getAsString(),
                        json.get("last_updated").getAsString()
                );
            } else {
                System.err.println("Failed to fetch tier data: HTTP " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching tier data: " + e.getMessage());
            return null;
        }
    }
}
