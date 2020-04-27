package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.logging.Level;

public class ExampleConfigCommand extends SubCommand {

    private BlockBoost bb;
//    private File file;
//    private FileConfiguration config;

    public ExampleConfigCommand(BlockBoost plugin) {
        bb = plugin;
//        file = new File(bb.getDataFolder(), "example_config.yml");
//        config = YamlConfiguration.loadConfiguration(file);
    }

//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (sender instanceof Player) return true;
//
//        if (file.exists() && file.length() != 0) {
//            sender.sendMessage("example_config.yml already exists! Replacing with default!");
//        } else {
//            sender.sendMessage("Creating example_config.yml!");
//        }
//
//        // TODO test https://bukkit.gamepedia.com/Configuration_API_Reference#Arbitrary_Configurations
//        reloadCustomConfig();
//        saveCustomConfig();
//        return true;
//    }



    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("bb.example")) {
            sender.sendMessage(ChatColor.RED + "You do not have the §4bb.example §cpermission!");
            return false;
        }

        bb.saveResource("example_config.yml", true);
        return true;
    }

    @Override
    public String name() {
        return "example";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

//    private void reloadCustomConfig() {
//        Reader defConfigStream = null;
//        try {
//            defConfigStream = new InputStreamReader(bb.getResource("example_config.yml"),
//                    "UTF8");
//        } catch (UnsupportedEncodingException e) {
//            bb.getLogger().log(Level.WARNING, "Failed to create example_config.yml", e);
//        }
//
//        if (defConfigStream != null) {
//            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
//            config.setDefaults(defConfig);
//        }
//    }
//
//    private void saveCustomConfig() {
//        if (config == null || file == null) reloadCustomConfig();
//
//        try {
//            config.save(file);
//        } catch (IOException ex) {
//            bb.getLogger().log(Level.SEVERE, "Could not save config to " + file,
//                    ex);
//        }
//    }
}
