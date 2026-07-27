package com.hopmod.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Zeigt die horizontale Geschwindigkeit (Blocks/s) oben links im HUD an,
 * ähnlich einem klassischen CS/Source Speedometer.
 */
public final class SpeedHud {

    private static final int COLOR_TEXT = 0xFFFFFF;
    private static final int PADDING_X = 6;
    private static final int PADDING_Y = 6;

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || client.options.hudHidden) {
            return;
        }

        Vec3d velocity = player.getVelocity();
        double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

        // Minecraft-Einheiten pro Tick -> Blocks pro Sekunde (20 Ticks/s).
        double blocksPerSecond = horizontalSpeed * 20.0;

        String text = String.format("Speed: %.2f blocks/s", blocksPerSecond);

        context.drawTextWithShadow(
                client.textRenderer,
                text,
                PADDING_X,
                PADDING_Y,
                COLOR_TEXT
        );
    }

    private SpeedHud() {
    }
}
