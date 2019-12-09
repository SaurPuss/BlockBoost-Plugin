package me.saurpuss.blockboost;

import me.saurpuss.blockboost.commands.BlockBoostCommand;
import me.saurpuss.blockboost.managers.BlockManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * BlockBoost Plugin for Spigot
 *
 * @author SaurPuss
 * Source: https://github.com/SaurPuss/BlockBoost-Plugin
 */
public final class BlockBoost extends JavaPlugin {

    private BlockManager blockManager;

    @Override
    public void onEnable() {
        // Load blockManager
        reloadManagers();

        if (blockManager == null) {
            getLogger().log(Level.SEVERE, "Block Manager failed to initialize during plugin " +
                    "startup! Disabling BlockBoost plugin!");
            getServer().getPluginManager().disablePlugin(this);
        }

        // Register reload command
        getCommand("blockboost").setExecutor(new BlockBoostCommand(this));
    }

    @Override
    public void onDisable() {
        // Unload active listeners
        HandlerList.unregisterAll(this);
    }

    public void reloadManagers() {
        if (blockManager != null)
            blockManager.unloadListeners();

        blockManager = new BlockManager(this);
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }
}