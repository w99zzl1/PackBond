# 🐾 PackBond

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.16.5-forge?style=for-the-badge&color=orange)
![Java Version](https://img.shields.io/badge/Java-8-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**PackBond** is a lightweight open-source tweak mod for **Minecraft 1.16.5 (Forge)** designed to overhaul tamed wolf AI, eliminate frustrating companion deaths, and enhance your control over the pack.

No more losing your best friends to unloaded chunks or accidental lava dives!

---

## ✨ Features

### 🌋 Advanced Lava Safety
* **Smart Pathfinding:** Wolves utilize a custom node processor (`LavaSafeNodeProcessor`) to actively detect and path around lava blocks.
* **Safe Combat:** Modified combat goals (`SafeMeleeAttackGoal`, `SafeLeapAtTargetGoal`) prevent wolves from accidentally leaping into lava or fires while attacking mobs.

### 🌌 Cross-Chunk Teleportation
* **Unload Proof:** If you fly away on Elytra or travel via fast mounts, your wolves will force-teleport to your position even if the chunk they were standing in becomes completely unloaded by the server.

### 🦴 The Pack Bone (Global Call)
* **Instant Recall:** A default item. Right-clicking the Bone instantly forces **all of your tamed wolves worldwide** to stand up and teleport directly to your location, regardless of whether they were ordered to sit.

---

## 🛠️ Code Architecture

The mod is written in **Java 8** for maximum compatibility with large 1.16.5 modpacks. Key core modifications include:
* `WolfAiHooks` & `WolfLavaSafety` — Core logic injections for dynamic environmental awareness.
* `LavaSafeNodeProcessor` — Custom path navigation context.

---

## 🚀 Installation & Requirements

1. Make sure you have **Minecraft Forge 1.16.5** installed.
2. Download the latest `.jar` file from [Releases](https://github.com) *(Replace with your link later)*.
3. Drop the file into your `mods` folder.

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE.txt](LICENSE.txt) file for details. You are completely free to use this code in your own projects or include this mod in any modpacks!
