package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.SpeedTask;
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

    private BlockBoost bb;
    public static HashMap<UUID, Long> additionCooldown;
    public static HashMap<UUID, Long> multiplierCooldown;
    public static HashMap<UUID, Long> multiplierBlockCooldown;

    public PlayerListener(BlockBoost plugin) {
        bb = plugin;
        bb.getServer().getPluginManager().registerEvents(this, bb);

        // Set up player time trackers
        additionCooldown = new HashMap<>();
        multiplierCooldown = new HashMap<>();
        multiplierBlockCooldown = new HashMap<>();

        registerOnlinePlayers();
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
        finishPlayerTasks(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        finishPlayerTasks(event.getPlayer());
    }

    public static boolean isOnAdditionCooldown(UUID player) {
        return additionCooldown.get(player) > System.currentTimeMillis() &&
                multiplierCooldown.get(player) > System.currentTimeMillis();
    }

    public static boolean isOnMultiplierCooldown(UUID player) {
        return additionCooldown.get(player) > System.currentTimeMillis() &&
                multiplierBlockCooldown.get(player) > System.currentTimeMillis();
    }

    private void finishPlayerTasks(Player player) {
        // If player is on cooldown check for pending task
        if (isOnMultiplierCooldown(player.getUniqueId()) ||
                isOnAdditionCooldown(player.getUniqueId())) {
            Bukkit.getScheduler().getPendingTasks().forEach(task -> {
                // player has a matching pending task
                if (task.getOwner().equals(bb) && ((SpeedTask) task).getPlayer() == player) {
                    ((Runnable) task).run();
                }
            });
        }

        // Clean up cooldown maps
        additionCooldown.remove(player.getUniqueId());
        multiplierCooldown.remove(player.getUniqueId());
        multiplierBlockCooldown.remove(player.getUniqueId());
    }

    public static void registerOnlinePlayers() {
        additionCooldown.clear();
        multiplierBlockCooldown.clear();
        multiplierBlockCooldown.clear();

        // Add players already online and set their time 2 seconds into the future
        Long time = System.currentTimeMillis() + 2000;
        for (Player player : Bukkit.getOnlinePlayers()) {
            additionCooldown.put(player.getUniqueId(), time);
            multiplierCooldown.put(player.getUniqueId(), time);
            multiplierBlockCooldown.put(player.getUniqueId(), time);
        }
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
