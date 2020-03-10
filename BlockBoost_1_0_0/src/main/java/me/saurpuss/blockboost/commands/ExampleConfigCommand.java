package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.logging.Level;

public class ExampleConfigCommand implements CommandExecutor {

    private BlockBoost bb;
    private File file;
    private FileConfiguration config;

    public ExampleConfigCommand(BlockBoost plugin) {
        bb = plugin;
        if (file == null) file = new File(bb.getDataFolder(), "example_config.yml");
        config = YamlConfiguration.loadConfiguration(file);

        // Look for defaults in the jar
        reloadCustomConfig();
        saveCustomConfig(); // TODO test https://bukkit.gamepedia.com/Configuration_API_Reference#Arbitrary_Configurations

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) return true;

        if (file.exists() && file.length() != 0) {
            sender.sendMessage("example_config.yml already exists! Replacing with default!");
        } else {
            sender.sendMessage("Creating exame_config.yml!");
        }

        reloadCustomConfig();
        saveCustomConfig();
        return true;
    }

    private void reloadCustomConfig() {
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(bb.getResource("example_config.yml"),
                    "UTF8");
        } catch (UnsupportedEncodingException e) {
            bb.getLogger().log(Level.WARNING, "Failed to create example_config.yml", e);
        }

        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);
        }
    }

    private void saveCustomConfig() {
        if (config == null || file == null) reloadCustomConfig();

        try {
            config.save(file);
        } catch (IOException ex) {
            bb.getLogger().log(Level.SEVERE, "Could not save config to " + file,
                    ex);
        }
    }
}
