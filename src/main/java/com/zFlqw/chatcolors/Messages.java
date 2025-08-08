package com.zFlqw.chatcolors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Messages {

    private final JavaPlugin plugin;
    private FileConfiguration messagesConfig;
    private String lang;

    public Messages(JavaPlugin plugin, String lang) {
        this.plugin = plugin;
        this.lang = lang.toLowerCase();
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
        this.lang = lang.toLowerCase();
        load();
    }

    public String getMessage(String key) {
        String msg = messagesConfig.getString(key, key);
        if (msg == null) return key;

        String prefix = messagesConfig.getString("prefix", "");
        msg = msg.replace("%prefix%", prefix);

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public List<String> getMessageList(String key) {
        if (messagesConfig.contains(key)) {
            List<String> list = messagesConfig.getStringList(key);
            if (list != null) {
                String prefix = messagesConfig.getString("prefix", "");
                List<String> coloredList = new ArrayList<>();
                for (String line : list) {
                    line = line.replace("%prefix%", prefix);
                    coloredList.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                return coloredList;
            }
        }
        return Collections.emptyList();
    }
}
