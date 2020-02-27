package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {

    private BlockBoost bb;

    ReloadCommand(BlockBoost plugin) {
        bb = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("bb.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have the §4bb.reload §cpermission!");
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "Reloading BlockBoost plugin!");
        sender.sendMessage(ChatColor.GRAY + "Completing pending tasks!");

        bb.doTasksNow();

        sender.sendMessage(ChatColor.GRAY + "Refreshing configuration files!");

        bb.reloadConfig();
        bb.reloadBB();

        sender.sendMessage(ChatColor.GREEN + "Finished reloading BlockBoost Plugin!");

        return true;
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
