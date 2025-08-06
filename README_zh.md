# ChatColors
[English Version](./README.md)

ChatColors 是一个轻量级的 Minecraft 插件，允许玩家在聊天中使用颜色代码（如 `&a`, `&b` 等），并可设置默认聊天颜色。支持开关、重载命令和多语言。

---

## 📦 功能

- 聊天支持颜色代码（如 `&a你好`）
- 设置默认聊天颜色
- 可开关颜色功能（config.yml）
- 多语言支持（简体中文 / 英语）
- 热重载配置文件

---

## 🛠 支持版本

- Minecraft 版本：1.18 ~ 1.21+ 
- Spigot API 版本：1.18 ~ 1.21+  
- Java 版本：Java 17 及以上  

---

## ⚙️ 配置文件（config.yml）

```yaml
enable-color: true
default-color: "&f"
language: "zh"
```

- `enable-color`: 是否启用聊天颜色
- `default-color`: 默认聊天颜色代码
- `language`: 使用的语言（`zh` 或 `en`）

---

## 🌐 语言支持

```
resources/
├── messages_zh.yml  # 简体中文
└── messages_en.yml  # English
```

---

## 🧪 命令

| 命令                 | 描述              | 权限            |
|----------------------|-------------------|-----------------|
| `/chatcolor reload`  | 重载配置文件      | `chatcolor.admin` |
| `/chatcolor set 颜色代码`| 设置默认聊天颜色  | `chatcolor.use` |

---

## 🔐 权限

```yaml
chatcolor.use:
  description: 允许使用 chatcolor 命令
  default: true
chatcolor.admin:
  description: 允许管理员重新加载配置文件
  default: false
```

---

## 📁 插件目录结构

<details>
<summary><code>目录结构 (点击展开)</code></summary>

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

## 🏗️ 构建方法

1. 安装 Java 17 和 Maven
2. 打开终端，进入项目目录
3. 执行以下命令：

```bash
mvn clean package
```

构建成功后，插件 `.jar` 位于：

```
target/ChatColors-version.jar
```

将其放入服务器的 `plugins/` 文件夹中即可使用。

---
