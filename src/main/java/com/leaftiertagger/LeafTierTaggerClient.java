package com.leaftiertagger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeafTierTaggerClient implements ClientModInitializer {
    private static final Map<String, TierData> playerTiers = new HashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long AUTO_UPDATE_INTERVAL = 30; // Auto-update every 30 seconds
    private static MinecraftClient client;

    @Override
    public void onInitializeClient() {
        System.out.println("LeafTierTagger Client initialized!");
        client = MinecraftClient.getInstance();
        
        // Register client command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("leaftiertagger")
                .executes(context -> {
                    // Show leaderboard menu
                    showLeaderboard(context);
                    return 1;
                })
                .then(ClientCommandManager.literal("update")
                    .executes(context -> {
                        updateAllPlayerTiers();
                        context.getSource().sendFeedback(Text.literal("§aUpdating all player tiers..."));
                        return 1;
                    }))
                .then(ClientCommandManager.literal("reload")
                    .executes(context -> {
                        playerTiers.clear();
                        updateAllPlayerTiers();
                        context.getSource().sendFeedback(Text.literal("§aReloading and updating all player tiers..."));
                        return 1;
                    }))
            );
        });
        
        // Start auto-update task
        scheduler.scheduleAtFixedRate(this::updateAllPlayerTiers, 0, AUTO_UPDATE_INTERVAL, TimeUnit.SECONDS);
        
        // Initial update on load
        updateAllPlayerTiers();
    }

    private void updateAllPlayerTiers() {
        if (client.world == null || client.player == null) return;
        
        CompletableFuture.runAsync(() -> {
            for (net.minecraft.entity.player.PlayerEntity player : client.world.getPlayers()) {
                if (!player.isGlowing()) { // Skip if not a real player
                    String username = player.getName().getString();
                    TierData tierData = TierApiClient.fetchPlayerTier(username);
                    if (tierData != null) {
                        playerTiers.put(username, tierData);
                        System.out.println("Updated tier for " + username + ": " + tierData.getTier());
                    }
                }
            }
        });
    }

    private void showLeaderboard(net.fabricmc.fabric.api.client.command.v2.ClientCommandContext context) {
        context.getSource().sendFeedback(Text.literal("§6§lLeafTierTagger Leaderboard"));
        context.getSource().sendFeedback(Text.literal("§7==================="));
        
        // Sort players by tier
        java.util.List<Map.Entry<String, TierData>> sortedPlayers = new java.util.ArrayList<>(playerTiers.entrySet());
        sortedPlayers.sort((a, b) -> compareTiers(b.getValue().getTier(), a.getValue().getTier()));
        
        // Show top 10
        int rank = 1;
        for (Map.Entry<String, TierData> entry : sortedPlayers) {
            if (rank > 10) break;
            
            String tier = entry.getValue().getTier();
            String username = entry.getKey();
            String color = getTierColorCode(tier);
            
            context.getSource().sendFeedback(Text.literal("§e#" + rank + " §r" + color + tier + " §7- §f" + username));
            rank++;
        }
        
        context.getSource().sendFeedback(Text.literal("§7==================="));
        context.getSource().sendFeedback(Text.literal("§aTotal players: " + playerTiers.size()));
    }

    private int compareTiers(String tier1, String tier2) {
        java.util.List<String> tierOrder = java.util.Arrays.asList("HT1", "LT1", "HT2", "LT2", "HT3", "LT3", "HT4", "LT4", "HT5", "LT5");
        int index1 = tierOrder.indexOf(tier1);
        int index2 = tierOrder.indexOf(tier2);
        return Integer.compare(index1, index2);
    }

    private String getTierColorCode(String tier) {
        switch (tier) {
            case "HT1":
            case "LT1":
                return "§6"; // Gold
            case "HT2":
            case "LT2":
                return "§c"; // Red
            case "HT3":
            case "LT3":
                return "§9"; // Blue
            case "HT4":
            case "LT4":
                return "§a"; // Green
            case "HT5":
            case "LT5":
                return "§d"; // Purple
            default:
                return "§f"; // White
        }
    }

    public static TierData getPlayerTier(String username) {
        return playerTiers.get(username);
    }

    public static void setPlayerTier(String username, TierData tier) {
        playerTiers.put(username, tier);
    }

    public static Map<String, TierData> getAllPlayerTiers() {
        return new HashMap<>(playerTiers);
    }
}
