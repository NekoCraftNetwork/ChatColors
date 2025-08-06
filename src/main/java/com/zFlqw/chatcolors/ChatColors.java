package com.zFlqw.chatcolors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ChatColors extends JavaPlugin implements Listener {

    private boolean enableColor;
    private String defaultColor;
    private String language;

    private Messages messages;
    private Map<String, String> playerColors = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfigSettings();

        messages = new Messages(this, language);

        getServer().getPluginManager().registerEvents(this, this);

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

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "==============================");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + messages.getMessage("plugin-disabled"));
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "==============================");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!enableColor) return;

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

        if (!sender.hasPermission("chatcolor.use")) {
            sender.sendMessage(ChatColor.RED + messages.getMessage("no-permission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + messages.getMessage("command-help"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfigSettings();
            messages.reload(language);
            sender.sendMessage(ChatColor.GREEN + messages.getMessage("config-reloaded"));
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
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
            sender.sendMessage(ChatColor.GREEN + messages.getMessage("set-color-success").replace("%color%", ChatColor.translateAlternateColorCodes('&', colorCode) + colorCode));
            return true;
        }

        sender.sendMessage(ChatColor.RED + messages.getMessage("unknown-command"));
        return true;
    }

    private boolean isValidColorCode(String code) {
        if (code.length() != 2) return false;
        if (code.charAt(0) != '&') return false;
        char c = Character.toLowerCase(code.charAt(1));
        return "0123456789abcdefklmnor".indexOf(c) >= 0;
    }
}