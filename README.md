# 🐾 PackBond

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.16.5-forge?style=for-the-badge&color=orange)
![Java Version](https://img.shields.io/badge/Java-8-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

---

## 🌐 Language / Язык

<details>
<summary><b>🇷🇺 Русский (Нажмите, чтобы открыть)</b></summary>
<br>

**PackBond** — лёгкий мод для 1.16.5, который предотвращает суицид волков в лаве и улучшает их телепорт. Теперь вам не придется возвращаться назад к Волкам потому что улетели слышком быстро и далеко!

В ваниле если лететь на элитрах, нужно постоянно возвращаться или ждать своих питомцев. Тут это исправлено. Мод ждет пока вы приземлитесь на безопасное место чтобы моментально телепортировать волков.

Волки теперь не глупые, они не лезут в лаву в агрессивном состоянии. Теперь из за скелета или овцы ваш питомец не погибнет.

### 📌 Что есть:
* 🌋 **Волки не лезут в лаву НИКОГДА.**
* 🌌 **Телепорт к игроку даже из выгруженных чанков.**
* 🦴 **Используйте ПКМ по “кость” для мгновенного сбора всех волков.**

---
</details>

<details>
<summary><b>🇬🇧 English (Click to expand)</b></summary>
<br>

**PackBond** is a lightweight 1.16.5 mod that prevents wolves from committing suicide in lava and improves their teleportation. Now you don't have to go back for your wolves just because you flew away too fast and too far!

In vanilla if you fly with elytra, you need to constantly go back or wait for your pets. This is fixed here. The mod waits until you land in a safe place to instantly teleport the wolves.

Wolves are no longer dumb, they don’t go into lava in aggressive state. Now because of skeleton or sheep your pet will not die.

### 📌 What it adds:
* 🌋 **Wolves do not enter lava EVER.**
* 🌌 **Teleport to player even from unloaded chunks.**
* 🦴 **Use RMB on “bone” for instant recall of all wolves.**

---
</details>

---

## 🛠️ Code Architecture

The mod is written in **Java 8** for full 1.16.5 modpack compatibility.

### 🧩 Core parts:
* `WolfAiHooks` & `WolfLavaSafety` — hooks for AI logic and environment checks.
* `LavaSafeNodeProcessor` — custom navigation handling for safer pathfinding.

---

## 🚀 Installation & Requirements

1. **Install** Minecraft Forge 1.16.5.
2. **Download** latest `.jar` from [Releases](https://github.com/w99zzl1/PackBond/releases/tag/v1.0.3).
3. **Put** it into your `mods` folder.

---

## 📄 License

**MIT License** — feel free to use it, modify it, or include it in modpacks. No restrictions.
