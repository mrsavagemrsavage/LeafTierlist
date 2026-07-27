# How to Compile LeafTierTagger Mod - Complete Guide

## Prerequisites

### Required Software:
1. **Java Development Kit (JDK) 17 or higher**
   - Download from: https://adoptium.net/
   - Install and add to PATH
   - Verify: `java -version` in Command Prompt

2. **Gradle** (included with the project)
   - The project includes Gradle wrapper
   - No separate installation needed

3. **Git** (optional, for version control)
   - Download from: https://git-scm.com/

## Step-by-Step Build Process

### Step 1: Navigate to Mod Directory
```bash
cd C:\Users\bbpro\CascadeProjects\LeafTierTagger\mod
```

### Step 2: Clean Previous Builds (if any)
```bash
.\gradlew.bat clean
```
This removes any previous build artifacts and cache issues.

### Step 3: Build the Mod
```bash
.\gradlew.bat build
```

This will:
- Download Minecraft files (first time only)
- Download Fabric dependencies
- Compile Java source code
- Create JAR file in `build/libs/`

### Step 4: Locate Compiled JAR
After successful build, find:
```
C:\Users\bbpro\CascadeProjects\LeafTierTagger\mod\build\libs\leaftiertagger-1.0.0.jar
```

## Troubleshooting Common Issues

### Issue 1: "gradlew.bat not recognized"
**Solution:**
- Make sure you're in the correct directory
- Use `.\gradlew.bat` (with backslash) not `gradlew.bat`

### Issue 2: "Failed to remap minecraft" / Download failures
**Solution:**
- Clear Gradle cache: `Remove-Item -Recurse -Force C:\Users\bbpro\.gradle\caches\fabric-loom`
- Try building again
- Check your internet connection
- Try using a VPN

### Issue 3: "Java not found" errors
**Solution:**
- Install JDK 17 from https://adoptium.net/
- Set JAVA_HOME environment variable
- Restart Command Prompt after installation

### Issue 4: "Plugin not found" errors
**Solution:**
- Delete `.gradle` folder in mod directory
- Delete `C:\Users\bbpro\.gradle\caches`
- Run build again

### Issue 5: Network timeout during downloads
**Solution:**
- Increase timeout in `gradle.properties` (create if doesn't exist):
```properties
org.gradle.daemon=true
org.gradle.jvmargs=-Xmx2048M
systemProp.http.proxyHost=
systemProp.http.proxyPort=
systemProp.https.proxyHost=
systemProp.https.proxyPort=
```

## Alternative Build Methods

### Method 1: Use IDE (IntelliJ IDEA)
1. Download IntelliJ IDEA Community Edition
2. Open the mod folder as a project
3. Let IntelliJ import Gradle project
4. Click "Build" -> "Build Project"
5. JAR will be in `build/libs/`

### Method 2: Manual Java Compilation
If Gradle fails completely:
```bash
# Compile Java files manually
javac -cp "path/to/fabric-api.jar" src/main/java/com/leaftiertagger/*.java

# Create JAR manually
jar cf leaftiertagger.jar -C src/main/resources .
```

### Method 3: GitHub Actions (Recommended)
1. Create GitHub repository
2. Upload mod files
3. Create `.github/workflows/build.yml`
4. GitHub will build automatically
5. Download JAR from Actions tab

## Understanding the Build Process

### What Gradle Does:
1. **Downloads Dependencies**: Gets Minecraft, Fabric Loader, Fabric API
2. **Remaps Minecraft**: Converts obfuscated Minecraft code
3. **Compiles Java**: Turns your code into bytecode
4. **Packages**: Creates JAR file with all dependencies

### Build Files Explanation:
- `build.gradle`: Build configuration and dependencies
- `settings.gradle`: Gradle settings
- `gradlew.bat`: Windows Gradle wrapper script
- `gradle/wrapper/`: Gradle wrapper files
- `src/main/java/`: Your Java source code
- `src/main/resources/`: Mod metadata and assets

## Current Build Configuration

The mod is currently configured for:
- **Minecraft Version**: 1.16.5
- **Fabric Loader**: 0.11.3
- **Fabric API**: 0.32.5+1.16
- **Java Version**: 8+

To change versions, edit `build.gradle` and `fabric.mod.json`.

## Quick Reference Commands

```bash
# Navigate to mod directory
cd C:\Users\bbpro\CascadeProjects\LeafTierTagger\mod

# Clean build
.\gradlew.bat clean

# Build mod
.\gradlew.bat build

# Build with debug info
.\gradlew.bat build --info

# Build with stacktrace on error
.\gradlew.bat build --stacktrace

# Clean all caches
Remove-Item -Recurse -Force .gradle
Remove-Item -Recurse -Force C:\Users\bbpro\.gradle\caches\fabric-loom
```

## Next Steps After Build

1. **Copy JAR to Minecraft mods folder**
   ```
   C:\Users\bbpro\AppData\Roaming\.minecraft\mods\
   ```

2. **Install Fabric Loader** (if not already installed)
   - Download from https://fabricmc.net/
   - Run installer for your Minecraft version

3. **Install Fabric API** (if not already installed)
   - Download from https://www.curseforge.com/minecraft/mc-mods/fabric-api
   - Place in mods folder

4. **Launch Minecraft**
   - Select Fabric profile
   - The mod should load automatically

## Getting Help

If build still fails:
1. Check Java version: `java -version`
2. Check internet connection
3. Try building from different network
4. Use GitHub Actions for automatic building
5. Contact Fabric community for build issues
