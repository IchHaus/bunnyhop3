package com.hopmod.client;

/**
 * Zentrale Stellschrauben für das Air-Strafe / Bhop Movement.
 * Werte sind an klassisches Quake/Source-Airstrafing angelehnt,
 * aber frei für Minecraft-Ticks (20/s) abgestimmt.
 */
public final class HopConfig {

    // Wie stark pro Tick in der Luft in Richtung der Wunschbewegung beschleunigt wird.
    // Höher = schnelleres Aufbauen von Speed beim sauberen Strafen.
    public static double airAccelerate = 12.0;

    // "Wish speed": Ziel-Grundgeschwindigkeit pro Tick, auf die accelerate hin arbeitet.
    // Bewusst klein gehalten, weil airAccelerate das eigentliche Tempo bestimmt.
    public static double airWishSpeed = 0.45;

    // KEIN Cap auf die horizontale Maximalgeschwindigkeit (auf Wunsch des Users).
    public static boolean enableSpeedCap = false;
    public static double maxHorizontalSpeed = Double.MAX_VALUE;

    // Reibung/Verlust beim Bodenkontakt zwischen zwei Hops (0 = kein Verlust).
    public static double groundFriction = 0.02;

    private HopConfig() {
    }
}
