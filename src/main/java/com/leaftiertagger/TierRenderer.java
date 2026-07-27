package com.leaftiertagger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class TierRenderer {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final float SCALE = 0.03f; // Clean scale like official tiertagger
    private static final float HEAD_OFFSET = 0.25f; // Position above head

    public static void renderTierAbovePlayer(PlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, double x, double y, double z) {
        if (player == null || client.player == null) return;

        // Don't render if too far away
        if (client.player.squaredDistanceTo(x, y, z) > 64) return;

        // Get player's tier data from cache
        String username = player.getName().getString();
        TierData tierData = LeafTierTaggerClient.getPlayerTier(username);
        if (tierData == null) return;

        String tier = tierData.getTier();
        if (tier == null || tier.equals("Unknown")) return;

        TextRenderer textRenderer = client.textRenderer;

        // Calculate position above head
        float headHeight = player.getHeight() + HEAD_OFFSET;

        matrices.push();
        matrices.translate(x, y + headHeight, z);
        matrices.multiply(client.gameRenderer.getCamera().getRotation());
        matrices.scale(-SCALE, -SCALE, SCALE);

        // Calculate text position
        float crystalSize = 8.0f;
        float textX = -crystalSize;
        float textY = 0f;

        // Draw 2D cartoonish green/white crystal icon (like official tiertagger)
        // Crystal outline (green)
        int greenColor = 0x55FF55;
        textRenderer.draw(matrices, Text.literal("◆"), textX, textY, greenColor);
        
        // Crystal inner (white)
        int whiteColor = 0xFFFFFF;
        textRenderer.draw(matrices, Text.literal("◇"), textX + 1, textY + 1, whiteColor);
        
        // Dark outline for better visibility
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    textRenderer.draw(matrices, Text.literal(" " + tier), textX + crystalSize + 2 + i, textY + j, 0x000000);
                }
            }
        }

        // Render tier text in tier color
        int tierColor = getTierColor(tier);
        textRenderer.draw(matrices, Text.literal(" " + tier), textX + crystalSize + 2, textY, tierColor);

        matrices.pop();
    }

    private static int getTierColor(String tier) {
        // Clean colors matching official tiertagger style
        switch (tier) {
            case "HT1": return 0xFFAA00; // Gold
            case "LT1": return 0xFFAA00; // Gold
            case "HT2": return 0xFF5555; // Red
            case "LT2": return 0xFF5555; // Red
            case "HT3": return 0x5555FF; // Blue
            case "LT3": return 0x5555FF; // Blue
            case "HT4": return 0x55FF55; // Green
            case "LT4": return 0x55FF55; // Green
            case "HT5": return 0xFF55FF; // Purple
            case "LT5": return 0xFF55FF; // Purple
            default: return 0xFFFFFF; // White
        }
    }
}
