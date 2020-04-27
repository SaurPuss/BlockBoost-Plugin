package me.saurpuss.blockboost;

import me.saurpuss.blockboost.commands.BlockBoostCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * BlockBoost Plugin for Spigot 1.13
 *
 * @author SaurPuss
 * Source: https://github.com/SaurPuss/BlockBoost-Plugin
 */
public final class BlockBoost extends JavaPlugin {

    private static BlockBoost instance;
    private BlockManager blockManager;

    @Override
    public void onEnable() {
        // For that static speed block plugin requirement
        instance = this;
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

    public static BlockBoost getInstance() {
        return instance;
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