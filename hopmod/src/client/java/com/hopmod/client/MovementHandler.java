package com.hopmod.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Eigene Air-Strafe- und Auto-Bhop-Implementierung.
 *
 * Prinzip (klassisches Quake/CS-Airstrafing, hier neu für MC-Ticks umgesetzt):
 * - Solange der Spieler in der Luft ist, wird die "Wunschrichtung" aus den
 *   gehaltenen Bewegungstasten relativ zur Blickrichtung berechnet.
 * - Die horizontale Geschwindigkeit wird jeden Tick ein Stück in Richtung
 *   dieser Wunschrichtung beschleunigt (accelerate-Formel), nicht direkt gesetzt.
 * - Der Beschleunigungsanteil hängt vom Winkel zwischen aktueller Bewegung und
 *   Wunschrichtung ab -> sauberes Maus+Taste-Timing baut mehr Speed auf.
 * - Es gibt bewusst kein Cap auf die resultierende Horizontalgeschwindigkeit.
 */
public final class MovementHandler {

    private static boolean wasOnGroundLastTick = true;

    public static void onClientTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }

        handleAutoJump(client, player);
        handleAirStrafe(client, player);

        wasOnGroundLastTick = player.isOnGround();
    }

    private static void handleAutoJump(MinecraftClient client, ClientPlayerEntity player) {
        boolean spaceHeld = client.options.jumpKey.isPressed();
        if (spaceHeld && player.isOnGround()) {
            player.jump();
        }
    }

    private static void handleAirStrafe(MinecraftClient client, ClientPlayerEntity player) {
        if (player.isOnGround()) {
            return;
        }

        double forward = 0.0;
        double strafe = 0.0;

        if (client.options.forwardKey.isPressed()) forward += 1.0;
        if (client.options.backKey.isPressed()) forward -= 1.0;
        if (client.options.rightKey.isPressed()) strafe -= 1.0;
        if (client.options.leftKey.isPressed()) strafe += 1.0;

        if (forward == 0.0 && strafe == 0.0) {
            return;
        }

        float yaw = player.getYaw();
        double yawRad = Math.toRadians(yaw);

        // Wunschrichtung relativ zur Blickrichtung (Yaw), normalisiert.
        double sinYaw = Math.sin(yawRad);
        double cosYaw = Math.cos(yawRad);

        double wishX = -sinYaw * forward + cosYaw * strafe;
        double wishZ = cosYaw * forward + sinYaw * strafe;

        double wishLength = Math.sqrt(wishX * wishX + wishZ * wishZ);
        if (wishLength < 1.0e-6) {
            return;
        }
        wishX /= wishLength;
        wishZ /= wishLength;

        Vec3d velocity = player.getVelocity();
        double currentSpeed = velocity.x * wishX + velocity.z * wishZ;

        double addSpeed = HopConfig.airWishSpeed - currentSpeed;
        if (addSpeed <= 0.0) {
            return;
        }

        double accelSpeed = HopConfig.airAccelerate * HopConfig.airWishSpeed;
        if (accelSpeed > addSpeed) {
            accelSpeed = addSpeed;
        }

        double newX = velocity.x + wishX * accelSpeed;
        double newZ = velocity.z + wishZ * accelSpeed;

        if (HopConfig.enableSpeedCap) {
            double horizontalSpeed = Math.sqrt(newX * newX + newZ * newZ);
            if (horizontalSpeed > HopConfig.maxHorizontalSpeed) {
                double scale = HopConfig.maxHorizontalSpeed / horizontalSpeed;
                newX *= scale;
                newZ *= scale;
            }
        }

        player.setVelocity(newX, velocity.y, newZ);
    }

    private MovementHandler() {
    }
}
