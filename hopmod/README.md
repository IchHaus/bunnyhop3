# HopMod

Eigene Fabric-Client-Mod für Minecraft 26.2: Auto-Bunnyhop + Air-Strafing ohne
Speed-Cap, plus Speed-HUD (Blocks/s oben links).

Komplett selbst geschrieben, nicht von anderen Mods kopiert.

## Funktionen

- **Auto-Bhop**: Space gedrückt halten -> springt automatisch bei jedem
  Bodenkontakt.
- **Air-Strafing**: A/D + Maus in der Luft bauen Geschwindigkeit auf,
  klassisches Quake/CS-Prinzip (accelerate-Formel statt direktem Setzen der
  Velocity).
- **Kein Speed-Cap**: Es gibt standardmäßig kein Limit für die maximale
  horizontale Geschwindigkeit.
- **Speed-HUD**: zeigt die aktuelle horizontale Geschwindigkeit in Blocks/s.
- Reines Client-Mod, kein Server-Code nötig (`environment: client`).

## Movement-Parameter anpassen

Alles Wichtige steht in `src/client/java/com/hopmod/client/HopConfig.java`:

```java
public static double airAccelerate = 12.0;   // wie schnell Speed aufgebaut wird
public static double airWishSpeed = 0.45;    // "Ziel"-Basisgeschwindigkeit pro Tick
public static boolean enableSpeedCap = false; // true = Cap wieder aktivieren
public static double maxHorizontalSpeed = Double.MAX_VALUE;
```

Wenn du es langsamer/schneller haben willst: `airAccelerate` hoch = schnelleres
Strafen. `airWishSpeed` hoch = höhere Basisgeschwindigkeit auch bei
mittelmäßigem Strafing.

## Build (GitHub Actions)

Einfach das Repo auf GitHub pushen (Branch `main`). Der Workflow unter
`.github/workflows/build.yml` baut automatisch und lädt den fertigen Jar als
Artifact hoch (`Actions` -> Workflow-Run -> `hopmod` Artifact).

## Installation

1. Fabric Loader >= 0.19.0 für Minecraft 26.2 installieren.
2. Fabric API 0.150.1+26.2 (oder neuer, passend zu 26.2) in den `mods`-Ordner.
3. Den gebauten `hopmod-*.jar` (aus `build/libs/`, nicht die `-sources.jar`)
   ebenfalls in `mods`.

## Hinweis

Client-only gebaut wie gewünscht. Für Multiplayer-Server müsste die
Geschwindigkeit serverseitig validiert/erlaubt werden, sonst wirft Anti-Cheat
oder Vanilla-Movement-Check dich ggf. zurück bzw. der Server ignoriert die
höhere Client-Velocity.
