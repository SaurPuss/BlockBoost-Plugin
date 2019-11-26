package me.saurpuss.blockboost;

import me.saurpuss.blockboost.commands.BlockBoostCommand;
import me.saurpuss.blockboost.managers.BBManager;
import me.saurpuss.blockboost.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockBoost extends JavaPlugin {

    private ConfigManager configManager;
    private BBManager bbManager;

    @Override
    public void onEnable() {
        // Register configManager
        loadConfigManager();

        // Register Block Listeners through the manager
        loadBBManager();

        // Register reload command
        getCommand("blockboost").setExecutor(new BlockBoostCommand(this));
    }

    @Override
    public void onDisable() {}

    public void loadConfigManager() {
        configManager = new ConfigManager(this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void loadBBManager() {
        bbManager = new BBManager(this);
    }

    public BBManager getBbManager() {
        return bbManager;
    }
}
