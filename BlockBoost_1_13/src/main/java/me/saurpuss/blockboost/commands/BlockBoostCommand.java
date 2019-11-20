package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlockBoostCommand implements CommandExecutor {

    private BlockBoost bb;

    public BlockBoostCommand(BlockBoost plugin) {
        bb = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1 && !args[0].equalsIgnoreCase("reload"))
            return false;

        // TODO logging


        // remove active eventlistener
        bb.getBbManager().unloadListeners();

        // reload config
        bb.reloadConfig();

        // assign new bbManager
        bb.setBbManager();

        return true;
    }
}
