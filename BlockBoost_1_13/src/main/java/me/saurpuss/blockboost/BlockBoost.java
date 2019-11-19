package me.saurpuss.blockboost;

import me.saurpuss.blockboost.events.VelocityBlockListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockBoost extends JavaPlugin {

    private static VelocityBlockListener eventListener;

    @Override
    public void onEnable() {
        // Register config.yml
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Register Block Listener
        registerBlockEffects(); // TODO move to event manager
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerBlockEffects() {
        eventListener = new VelocityBlockListener(this);
    }

    public void reloadBlockEffects() {
        eventListener.unregister();
        registerBlockEffects();
    }
}
