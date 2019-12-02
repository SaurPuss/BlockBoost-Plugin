package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.BounceBlock;
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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

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
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        String world = player.getWorld().getName();

        BLOCKS.forEach((key, mat) -> {
            if (key == block.getType()) {
                PotionEffectBlock material = (PotionEffectBlock) mat;
                // material has an inclusion and it's global or this world
                if (material.isIncludeWorld() &&
                        (material.getWorld().equalsIgnoreCase("global") ||
                                material.getWorld().equalsIgnoreCase(world))) {
                    triggerPotionEffect(player, material);
                }
                // material has an exclusion that is not global and is not this world
                else if (!material.isIncludeWorld() &&
                        !material.getWorld().equalsIgnoreCase("global") &&
                        !material.getWorld().equalsIgnoreCase(world)) {
                    triggerPotionEffect(player, material);
                }
            }
        });
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
