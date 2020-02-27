package me.saurpuss.blockboost.util;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpeedResetTask extends BukkitRunnable {

    // TODO everything

    private Player player;
    private float speed;

    public SpeedResetTask(Player player, float speed) {
        this.player = player;
        this.speed = speed;
    }

    @Override
    public void run() {
        player.setWalkSpeed(speed);
        SpeedListener.finishTask(player.getUniqueId());
    }

    public float getSpeed() {
        return speed;
    }
}
