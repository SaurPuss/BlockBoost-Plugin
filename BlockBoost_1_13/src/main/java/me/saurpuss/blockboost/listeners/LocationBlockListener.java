package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.BounceBlock;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Optional;

public class LocationBlockListener extends AbstractListener implements Listener {

    private final HashMap<Location, AbstractBlock> BLOCKS;

    public LocationBlockListener(BlockBoost plugin, HashMap<Location, AbstractBlock> blocks) {
        // TODO everything

        Optional<AbstractBlock> test = blocks.values().stream().findFirst();

        // Test validity of the blocks before registering listener
        if (test.isPresent() && test.get() instanceof BounceBlock) {
            BLOCKS = blocks;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else {
            BLOCKS = null;
        }
    }


    @EventHandler
    public void onBlockLocate(PlayerMoveEvent event) {
        // Check if player is allowed to activate
        final Player player = event.getPlayer();
        if (player.hasPermission("bb.deny"))
            return;

        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR)
            block = block.getRelative(BlockFace.DOWN);

        Location location = block.getLocation();
        if (!BLOCKS.containsKey(location)) return;

        // TODO get block type and defer do action
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
