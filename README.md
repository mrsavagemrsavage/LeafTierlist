# LeafTierTagger Minecraft Mod

A Fabric mod for Minecraft 1.21.1 that displays player tiers above their heads in-game, integrated with the LeafTierTagger API.

## Features

- **Tier Display**: Shows player tiers above their heads in-game
- **Auto-Update**: Automatically fetches tier data every 30 seconds
- **HUD Display**: Shows your current tier in the HUD
- **Client Commands**: Manual update and reload commands
- **Color-Coded Tiers**: Different colors for different tier levels

## Installation

1. Install Fabric Loader for Minecraft 1.21.1
2. Install Fabric API
3. Place the `leaftiertagger-1.0.0.jar` in your `mods` folder
4. Make sure the LeafTierTagger API is running (default: http://localhost:5000)

## Commands

- `/leaftiertagger update` - Manually update all player tiers
- `/leaftiertagger reload` - Clear cache and reload all player tiers

## Configuration

The mod connects to the LeafTierTagger API at `http://localhost:5000` by default. You can change this in `LeafTierTagger.java`:

```java
public static final String API_URL = "http://localhost:5000/api/player/";
```

## Tier Colors

- **HT1/LT1**: Gold (#FFAA00)
- **HT2/LT2**: Red (#FF5555)
- **HT3/LT3**: Blue (#5555FF)
- **HT4/LT4**: Green (#55FF55)
- **HT5/LT5**: Purple (#FF55FF)

## Building

```bash
cd mod
./gradlew build
```

The JAR file will be in `mod/build/libs/`.

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.15.11+
- Fabric API
- Java 21
- LeafTierTagger API server running
