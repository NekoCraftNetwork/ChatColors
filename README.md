# ChatColors
[中文版本（简体）](./README_zh.md)

ChatColors is a lightweight Minecraft plugin that allows players to use color codes (e.g., `&a`, `&b`) in chat messages and set a default color. It supports enabling/disabling features, reloading configs, and multilingual support.

---

## 📦 Features

- Chat supports color codes (e.g., `&aHello`)
- Set a default chat color
- Enable/disable color via config
- Multi-language support (English / Simplified Chinese)
- Hot-reload config and language files

---

## 🛠 Supported Versions

- Minecraft versions: 1.18 ~ 1.21+  
- Spigot API versions: 1.18 ~ 1.21+  
- Java versions: Java 17 or higher

---

## ⚙️ Configuration (`config.yml`)

```yaml
enable-color: true
default-color: "&f"
language: "en"
```

- `enable-color`: Enable or disable colored chat
- `default-color`: Default color code for chat
- `language`: Language file to use (`en` or `zh`)

---

## 🌐 Language Files

```
resources/
├── messages_zh.yml  # Simplified Chinese
└── messages_en.yml  # English
```

---

## 🧪 Commands

| Command                   | Description                    | Permission       |
|---------------------------|--------------------------------|------------------|
| `/chatcolor reload`       | Reload config and lang files   | `chatcolor.admin`  |
| `/chatcolor set <color>`  | Set default chat color         | `chatcolor.use`  |

---

## 🔐 Permissions

```yaml
chatcolor.use:
  description: Allows use of chatcolor commands
  default: true
chatcolor.admin:
  description: Allows admin to use the reload command to reload configuration.
  default: false
```

---

## 📁 Project Structure
<details>
<summary><code>Project Structure (click to expand)</code></summary>

```
ChatColor/
├── LICENSE
├── README.md
├── README_zh.md
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── zFlqw/
        │           └── chatcolors/
        │               ├── ChatColors.java
        │               └── Messages.java
        └── resources/
            ├── plugin.yml
            ├── config.yml
            ├── messages_en.yml
            └── messages_zh.yml
```

</details>

---

## 🏗️ How to Build

1. Install Java 17 and Maven
2. Open terminal and navigate to the project directory
3. Run:

```bash
mvn clean package
```

The final plugin `.jar` will be located at:

```
target/ChatColors-version.jar
```

Copy it into your server's `plugins/` folder to use.

---
