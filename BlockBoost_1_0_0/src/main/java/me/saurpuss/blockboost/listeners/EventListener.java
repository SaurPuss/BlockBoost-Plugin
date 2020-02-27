package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.blocks.single.SpeedBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Handle speed related storage
        SpeedBlock.resetSpeedNow(event.getPlayer());
        SpeedBlock.removeSpeedTask(event.getPlayer().getUniqueId());
        SpeedBlock.removeSpeedCooldown(event.getPlayer().getUniqueId());

    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        // Handle speed related storage
        SpeedBlock.resetSpeedNow(event.getPlayer());
        SpeedBlock.removeSpeedTask(event.getPlayer().getUniqueId());
        SpeedBlock.removeSpeedCooldown(event.getPlayer().getUniqueId());
    }
}
