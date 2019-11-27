package me.saurpuss.blockboost;

import me.saurpuss.blockboost.commands.BlockBoostCommand;
import me.saurpuss.blockboost.managers.BlockManager;
import me.saurpuss.blockboost.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockBoost extends JavaPlugin {

    private ConfigManager configManager;
    private BlockManager blockManager;

    @Override
    public void onEnable() {
        // Load Managers
        loadConfigManager();
        loadBlockManager();

        // Register reload command
        getCommand("blockboost").setExecutor(new BlockBoostCommand(this));
    }

    @Override
    public void onDisable() {}

    public void reloadManagers() {
        configManager = new ConfigManager(this);
        blockManager.unloadListeners();
        blockManager = new BlockManager(this);
    }

    private void loadConfigManager() {
        configManager = new ConfigManager(this);
    }

    private void loadBlockManager() {
        blockManager = new BlockManager(this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }
}
