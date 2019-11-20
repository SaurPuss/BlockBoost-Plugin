package me.saurpuss.blockboost;

import me.saurpuss.blockboost.commands.BlockBoostCommand;
import me.saurpuss.blockboost.util.BBManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockBoost extends JavaPlugin {

    private static BBManager bbManager;

    @Override
    public void onEnable() {
        // Register config.yml
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Register Block Listener
        bbManager = new BBManager(this);

        // Register reload command
        getCommand("bb").setExecutor(new BlockBoostCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BBManager getBbManager() {
        return bbManager;
    }
    public void setBbManager() {
        bbManager = new BBManager(this);
    }
}
