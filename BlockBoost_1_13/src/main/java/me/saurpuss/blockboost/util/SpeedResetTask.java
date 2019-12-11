package me.saurpuss.blockboost.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class SpeedResetTask extends BukkitRunnable {

    private Player player;
    private float speed;

    public SpeedResetTask(Player player, float speed) {
        this.player = player;
        this.speed = speed;
    }

    @Override
    public void run() {
        player.sendMessage("SpeedTask speed: " + speed);
        Bukkit.getServer().getLogger().log(Level.INFO, "Running SpeedTask on " + player.getName());
        player.setWalkSpeed(speed);
    }

    public Player getPlayer() {
        return player;
    }
}
