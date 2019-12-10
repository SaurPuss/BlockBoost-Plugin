package me.saurpuss.blockboost.util;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpeedTask extends BukkitRunnable {

    private Player player;
    private float speed;

    public SpeedTask(Player player, float speed) {
        this.player = player;
        this.speed = speed;
    }

    @Override
    public void run() {
        player.setWalkSpeed(speed);
    }

    public Player getPlayer() { return player; }
}
