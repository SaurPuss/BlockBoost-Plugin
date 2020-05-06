package me.saurpuss.blockboost;

import me.saurpuss.blockboost.commands.BlockBoostCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * BlockBoost Plugin for Spigot 1.13
 *
 * @author SaurPuss
 * Source: https://github.com/SaurPuss/BlockBoost-Plugin
 */
public final class BlockBoost extends JavaPlugin {

    private BlockManager blockManager;

    @Override
    public void onEnable() {
        blockManager = new BlockManager(this);

        // Default config setup
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Register plugin commands & events
        getCommand("blockboost").setExecutor(new BlockBoostCommand(this));

        // TODO worldguard depend for region specific effects, maybe API hook or extension plugin?
    }

    @Override
    public void onDisable() {
        // Unload active listeners
        HandlerList.unregisterAll(this);
    }

    /**
     * Unregister any existing BlockBoost event listeners and create a new BlockManager
     */
    public void reloadBB() {
        if (blockManager != null)
            HandlerList.unregisterAll(this);

        blockManager = new BlockManager(this);
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }
}