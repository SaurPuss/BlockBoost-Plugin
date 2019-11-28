package me.saurpuss.blockboost;

import me.saurpuss.blockboost.commands.BlockBoostCommand;
import me.saurpuss.blockboost.managers.BlockManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockBoost extends JavaPlugin {

    private BlockManager blockManager;

    @Override
    public void onEnable() {
        // Load Managers
        loadBlockManager();

        // Register reload command
        getCommand("blockboost").setExecutor(new BlockBoostCommand(this));
    }

    @Override
    public void onDisable() {}

    public void reloadManagers() {
        blockManager.unloadListeners();
        blockManager = new BlockManager(this);
    }


    private void loadBlockManager() {
        blockManager = new BlockManager(this);
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }
}
