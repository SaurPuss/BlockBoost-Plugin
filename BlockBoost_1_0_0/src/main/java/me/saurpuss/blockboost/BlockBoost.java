package me.saurpuss.blockboost;

import me.saurpuss.blockboost.commands.BlockBoostCommand;
import me.saurpuss.blockboost.listeners.EventListener;
import me.saurpuss.blockboost.util.tasks.SpeedResetTask;
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
        // TODO only if speedblocks are involved
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // TODO create example_config.yml

        // TODO worldguard depend for region specific effects, maybe API hook or extension plugin?


    }

    @Override
    public void onDisable() {
        // Finish pending BB Tasks
        doTasksNow();

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

    /**
     * Make any pending tasks scheduled by this BlockBoost run now instead of later.
     */
    public void doTasksNow() {
        Bukkit.getScheduler().getPendingTasks().forEach(task -> {
            if (task.getOwner().equals(this)) {
                Player player = ((SpeedResetTask) task).getPlayer();
                float speed = ((SpeedResetTask) task).getSpeed();
                getLogger().log(Level.INFO,
                        "Completing Speed Reset task for id " + task.getTaskId() + "! Setting " +
                                "speed for player " + player.getName() + " to " + speed + "!");
                player.setWalkSpeed(speed);
            }
        });
    }
}