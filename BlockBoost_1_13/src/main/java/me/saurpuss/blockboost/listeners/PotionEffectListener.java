package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.PotionEffectBlock;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractListener;
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

public class PotionEffectListener extends AbstractListener implements Listener {

    private final HashMap<Material, AbstractBlock> BLOCKS;

    public PotionEffectListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();

        if (test.isPresent() && test.get() instanceof PotionEffectBlock) {
            BLOCKS = blocks;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else {
            BLOCKS = null;
        }
    }

    @EventHandler
    public void activateBlock(PlayerMoveEvent event) {
        Block block = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (!BLOCKS.containsKey(block.getType())) return;

        Player player = event.getPlayer();
        PotionEffectBlock material = (PotionEffectBlock) BLOCKS.get(block.getType());

        if (material.isIncludeWorld() &&
                (material.getWorld().equalsIgnoreCase("global") ||
                        material.getWorld().equalsIgnoreCase(player.getWorld().getName()))) {
            triggerPotionEffect(player, material);
        }
        // material has an exclusion that is not global and is not this world
        else if (!material.isIncludeWorld() &&
                !material.getWorld().equalsIgnoreCase("global") &&
                !material.getWorld().equalsIgnoreCase(player.getWorld().getName())) {
            triggerPotionEffect(player, material);
        }
    }

    private void triggerPotionEffect(Player player, PotionEffectBlock potionEffectBlock) {

        // TODO make this a thing
//        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1));


    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
