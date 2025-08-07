package com.zFlqw.chatcolors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatColors extends JavaPlugin implements Listener {

    private boolean enableColor;
    private String defaultColor;
    private String language;

    private Messages messages;
    private final Map<String, String> playerColors = new HashMap<>();

    private File playerDataFile;
    private FileConfiguration playerDataConfig;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfigSettings();

        messages = new Messages(this, language);

        playerDataFile = new File(getDataFolder(), "playerdata.yml");
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Could not create playerdata.yml file!");
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

        loadPlayerColors();

        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("chatcolor").setTabCompleter(this);

        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "==============================");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + messages.getMessage("plugin-loaded"));
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "==============================");
    }

    private void reloadConfigSettings() {
        reloadConfig();
        enableColor = getConfig().getBoolean("enable-color", true);
        defaultColor = getConfig().getString("default-color", "&f");
        language = getConfig().getString("language", "zh").toLowerCase();
    }

    private void loadPlayerColors() {
        playerColors.clear();
        if (playerDataConfig.contains("colors")) {
            for (String playerName : playerDataConfig.getConfigurationSection("colors").getKeys(false)) {
                playerColors.put(playerName, playerDataConfig.getString("colors." + playerName));
            }
        }
    }

    private void savePlayerColors() {
        for (Map.Entry<String, String> entry : playerColors.entrySet()) {
            playerDataConfig.set("colors." + entry.getKey(), entry.getValue());
        }
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            getLogger().severe("Could not save playerdata.yml file!");
        }
    }

    @Override
    public void onDisable() {
        savePlayerColors();

        getServer().getConsoleSender().sendMessage(ChatColor.RED + "==============================");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + messages.getMessage("plugin-disabled"));
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "==============================");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!enableColor)
            return;

        Player player = event.getPlayer();

        String colorCode = playerColors.getOrDefault(player.getName(), defaultColor);
        String color = ChatColor.translateAlternateColorCodes('&', colorCode);

        String message = ChatColor.translateAlternateColorCodes('&', event.getMessage());
        event.setMessage(color + message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("chatcolor")) {
            return false;
        }

        if (args.length == 0) {
            if (!sender.hasPermission("chatcolor.use")) {
                sender.sendMessage(ChatColor.RED + messages.getMessage("no-permission"));
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + messages.getMessage("command-help"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("chatcolor.admin")) {
                sender.sendMessage(ChatColor.RED + messages.getMessage("no-permission"));
                return true;
            }
            reloadConfigSettings();
            messages.reload(language);

            playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
            loadPlayerColors();
            sender.sendMessage(ChatColor.GREEN + messages.getMessage("config-reloaded"));
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("chatcolor.use")) {
                sender.sendMessage(ChatColor.RED + messages.getMessage("no-permission"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + messages.getMessage("only-player"));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + messages.getMessage("usage-set"));
                return true;
            }
            String colorCode = args[1];
            if (!isValidColorCode(colorCode)) {
                sender.sendMessage(ChatColor.RED + messages.getMessage("invalid-color"));
                return true;
            }
            playerColors.put(sender.getName(), colorCode);

            savePlayerColors();
            String coloredSample = ChatColor.translateAlternateColorCodes('&', colorCode) + colorCode;
            sender.sendMessage(
                    ChatColor.GREEN + messages.getMessage("set-color-success").replace("%color%", coloredSample));
            return true;
        }

        sender.sendMessage(ChatColor.RED + messages.getMessage("unknown-command"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("chatcolor")) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>();

            if (sender.hasPermission("chatcolor.use")) {
                subCommands.add("set");
            }
            if (sender.hasPermission("chatcolor.admin")) {
                subCommands.add("reload");
            }
            StringUtil.copyPartialMatches(args[0], subCommands, completions);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            if (sender.hasPermission("chatcolor.use")) {
                List<String> colorCodes = Arrays.asList(
                        "&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9",
                        "&a", "&b", "&c", "&d", "&e", "&f",
                        "&k", "&l", "&m", "&n", "&o", "&r");
                StringUtil.copyPartialMatches(args[1], colorCodes, completions);
            }
        }

        Collections.sort(completions);
        return completions;
    }

    private boolean isValidColorCode(String code) {
        if (code == null || code.length() != 2)
            return false;
        if (code.charAt(0) != '&')
            return false;
        char c = Character.toLowerCase(code.charAt(1));
        return "0123456789abcdefklmnor".indexOf(c) >= 0;
    }
}