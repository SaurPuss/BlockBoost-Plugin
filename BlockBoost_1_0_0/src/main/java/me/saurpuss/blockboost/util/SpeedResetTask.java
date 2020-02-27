package me.saurpuss.blockboost.util;

import me.saurpuss.blockboost.blocks.basic.SpeedBlock;
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

        // TODO double check & do I want this to  be a weak reference?
        SpeedBlock.nullMapEntry(player.getUniqueId());
    }

    public float getSpeed() {
        return speed;
    }
    public Player getPlayer() { return player; }
}
