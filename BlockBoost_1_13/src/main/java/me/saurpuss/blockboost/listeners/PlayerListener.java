package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.managers.BlockManager;
import me.saurpuss.blockboost.util.SpeedResetTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private BlockBoost bb;

    public PlayerListener(BlockBoost plugin) {
        bb = plugin;
        bb.getServer().getPluginManager().registerEvents(this, bb);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        finishPlayerTasks(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        finishPlayerTasks(event.getPlayer());
    }

    private void finishPlayerTasks(Player player) {
        // If player is on cooldown check for pending task
        if (BlockManager.isOnSpeedTaskCooldown(player.getUniqueId())) {
            Bukkit.getScheduler().getPendingTasks().forEach(task -> {
                // player has a matching pending task
                if (task.getOwner().equals(bb) && ((SpeedResetTask) task).getPlayer() == player) {
                    ((SpeedResetTask) task).run();
                }
            });
        }

        // Clean up cooldown maps
        BlockManager.speedTaskCooldown.remove(player.getUniqueId());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
