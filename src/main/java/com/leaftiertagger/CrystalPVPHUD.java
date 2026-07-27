package com.leaftiertagger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CrystalPVPHUD {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    
    // HUD Configuration
    public static int hudX = 5;
    public static int hudY = 5;
    public static boolean showNearbyTiers = true;
    public static boolean showMyTier = true;
    public static boolean showTierCount = true;
    public static float hudScale = 1.0f;

    public static void renderHUD(net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.DrawContext drawContext) {
        if (client.player == null) return;

        int currentY = hudY;

        // Show player's own tier
        if (showMyTier) {
            TierData myTier = LeafTierTaggerClient.getPlayerTier(client.player.getName().getString());
            if (myTier != null) {
                String tierText = "§l§6Your Tier: §r" + getTierColorCode(myTier.getTier()) + myTier.getTier();
                Text text = Text.literal(tierText);
                drawContext.drawText(client.textRenderer, text, hudX, currentY, 0xFFFFFF, true);
                currentY += 15;
            }
        }

        // Show nearby player tiers (CrystalPVP useful info)
        if (showNearbyTiers && client.world != null) {
            int nearbyCount = 0;
            int highTierCount = 0;
            
            for (net.minecraft.entity.player.PlayerEntity player : client.world.getPlayers()) {
                if (player != client.player && client.player.squaredDistanceTo(player) < 50) {
                    nearbyCount++;
                    TierData tierData = LeafTierTaggerClient.getPlayerTier(player.getName().getString());
                    if (tierData != null && isHighTier(tierData.getTier())) {
                        highTierCount++;
                    }
                }
            }

            if (showTierCount && nearbyCount > 0) {
                String countText = "§l§eNearby: §r§f" + nearbyCount + " §7(§c" + highTierCount + " High Tier§7)";
                Text text = Text.literal(countText);
                drawContext.drawText(client.textRenderer, text, hudX, currentY, 0xFFFFFF, true);
                currentY += 15;
            }

            // Show top 3 nearest high tier players
            if (nearbyCount > 0) {
                currentY = renderNearbyHighTiers(drawContext, currentY, 3);
            }
        }
    }

    private static int renderNearbyHighTiers(net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.DrawContext drawContext, int startY, int maxPlayers) {
        if (client.world == null) return startY;

        int currentY = startY;
        int rendered = 0;

        // Sort nearby players by distance
        java.util.List<net.minecraft.entity.player.PlayerEntity> nearbyPlayers = new java.util.ArrayList<>();
        for (net.minecraft.entity.player.PlayerEntity player : client.world.getPlayers()) {
            if (player != client.player && client.player.squaredDistanceTo(player) < 50) {
                nearbyPlayers.add(player);
            }
        }

        nearbyPlayers.sort((a, b) -> Float.compare(client.player.squaredDistanceTo(a), client.player.squaredDistanceTo(b)));

        for (net.minecraft.entity.player.PlayerEntity player : nearbyPlayers) {
            if (rendered >= maxPlayers) break;

            TierData tierData = LeafTierTaggerClient.getPlayerTier(player.getName().getString());
            if (tierData != null && isHighTier(tierData.getTier())) {
                double distance = Math.sqrt(client.player.squaredDistanceTo(player));
                String tierText = "§c" + tierData.getTier() + " §7" + player.getName().getString() + " §f(" + String.format("%.1f", distance) + "m)";
                Text text = Text.literal(tierText);
                drawContext.drawText(client.textRenderer, text, hudX, currentY, 0xFFFFFF, true);
                currentY += 12;
                rendered++;
            }
        }

        return currentY;
    }

    private static boolean isHighTier(String tier) {
        return tier.equals("HT1") || tier.equals("LT1") || tier.equals("HT2") || tier.equals("LT2");
    }

    private static String getTierColorCode(String tier) {
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

    // Configuration methods
    public static void setHudPosition(int x, int y) {
        hudX = x;
        hudY = y;
    }

    public static void setShowNearbyTiers(boolean show) {
        showNearbyTiers = show;
    }

    public static void setShowMyTier(boolean show) {
        showMyTier = show;
    }

    public static void setShowTierCount(boolean show) {
        showTierCount = show;
    }

    public static void setHudScale(float scale) {
        hudScale = scale;
    }
}
