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
    static HashMap<UUID, Long> additionCooldown;
    static HashMap<UUID, Long> multiplierCooldown;
    static HashMap<UUID, Long> multiplierBlockCooldown;

    public PlayerListener(BlockBoost plugin) {
        bb = plugin;
        bb.getServer().getPluginManager().registerEvents(this, bb);

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
        Player player = event.getPlayer();

        if (isOnMultiplierCooldown(player.getUniqueId()) ||
                isOnAdditionCooldown(player.getUniqueId()))
            doTaskNow(player);

        additionCooldown.remove(player.getUniqueId());
        multiplierCooldown.remove(player.getUniqueId());
        multiplierBlockCooldown.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();

        if (isOnMultiplierCooldown(player.getUniqueId()) ||
                isOnAdditionCooldown(player.getUniqueId()))
            doTaskNow(player);

        additionCooldown.remove(player.getUniqueId());
        multiplierCooldown.remove(player.getUniqueId());
        multiplierBlockCooldown.remove(player.getUniqueId());
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

    private void doTaskNow(Player player) {
        Bukkit.getScheduler().getPendingTasks().forEach(task -> {
            // player has a matching pending task
            if (task.getOwner().equals(bb) && ((SpeedTask) task).getPlayer() == player) {
                ((Runnable) task).run();
            }
        });
    }

}
