package com.zFlqw.chatcolors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Messages {

    private JavaPlugin plugin;
    private FileConfiguration messagesConfig;
    private String lang;

    public Messages(JavaPlugin plugin, String lang) {
        this.plugin = plugin;
        this.lang = lang;
        load();
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "messages_" + lang + ".yml");
        if (!file.exists()) {
            plugin.saveResource("messages_" + lang + ".yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(file);
    }

    public void reload(String lang) {
        this.lang = lang;
        load();
    }

    public String getMessage(String key) {
        String msg = messagesConfig.getString(key, key);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}