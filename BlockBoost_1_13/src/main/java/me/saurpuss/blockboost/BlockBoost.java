package me.saurpuss.blockboost;

import me.saurpuss.blockboost.commands.BlockBoostCommand;
import me.saurpuss.blockboost.commands.ForceSpeed;
import me.saurpuss.blockboost.managers.BlockManager;
import org.bukkit.Bukkit;
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
        reloadBB();

        if (blockManager == null) {
            getLogger().log(Level.SEVERE, "Block Manager failed to initialize during plugin " +
                    "startup! Disabling BlockBoost plugin!");
            getServer().getPluginManager().disablePlugin(this);
        }

        // Register plugin commands
        getCommand("blockboost").setExecutor(new BlockBoostCommand(this));
        getCommand("bbspeed").setExecutor(new ForceSpeed());
    }

    @Override
    public void onDisable() {
        // Finish pending BB Tasks
        doTasksNow();

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

    /**
     * Make any pending tasks scheduled by this BlockBoost run now instead of later.
     */
    public void doTasksNow() {
        Bukkit.getScheduler().getPendingTasks().forEach(task -> {
            if (task.getOwner().equals(this)) {
                ((Runnable) task).run();
            }
        });
    }
}