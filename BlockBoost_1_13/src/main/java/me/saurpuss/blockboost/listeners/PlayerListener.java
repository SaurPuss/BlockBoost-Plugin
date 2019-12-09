package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerListener implements Listener {

    static HashMap<UUID, Long> additionCooldown;
    static HashMap<UUID, Long> multiplierCooldown;
    static HashMap<UUID, Long> multiplierBlockCooldown;

    public PlayerListener(BlockBoost plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Set up player time trackers
        additionCooldown = new HashMap<>();
        multiplierCooldown = new HashMap<>();
        multiplierBlockCooldown = new HashMap<>();

        // Add players already online
        Long time = System.currentTimeMillis() + 2000;
        for (Player player : Bukkit.getOnlinePlayers()) {
            additionCooldown.put(player.getUniqueId(), time);
            multiplierCooldown.put(player.getUniqueId(), time);
            multiplierBlockCooldown.put(player.getUniqueId(), time);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Long time = System.currentTimeMillis();

        additionCooldown.put(uuid, time);
        multiplierCooldown.put(uuid, time);
        multiplierBlockCooldown.put(uuid, time);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        additionCooldown.remove(uuid);
        multiplierCooldown.remove(uuid);
        multiplierBlockCooldown.remove(uuid);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        additionCooldown.remove(uuid);
        multiplierCooldown.remove(uuid);
        multiplierBlockCooldown.remove(uuid);
    }

    public static boolean isOnAdditionCooldown(UUID player) {
        return additionCooldown.get(player) > System.currentTimeMillis() &&
                multiplierCooldown.get(player) > System.currentTimeMillis();
    }

    public static boolean isOnMultiplierCooldown(UUID player) {
        return additionCooldown.get(player) > System.currentTimeMillis() &&
                multiplierBlockCooldown.get(player) > System.currentTimeMillis();
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

}
