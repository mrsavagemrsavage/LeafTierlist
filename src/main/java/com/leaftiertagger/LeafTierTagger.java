package com.leaftiertagger;

import net.fabricmc.api.ModInitializer;

public class LeafTierTagger implements ModInitializer {
    public static final String MOD_ID = "leaftiertagger";
    public static final String API_URL = "http://localhost:5000/api/player/";

    @Override
    public void onInitialize() {
        System.out.println("LeafTierTagger initialized!");
    }
}
