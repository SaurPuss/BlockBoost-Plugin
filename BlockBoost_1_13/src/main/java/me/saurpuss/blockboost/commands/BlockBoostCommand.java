package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BlockBoostCommand implements CommandExecutor {

    private final BlockBoost bb;

    public BlockBoostCommand(BlockBoost plugin) {
        bb = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bb.admin"))
            return Bukkit.dispatchCommand(sender, "version blockboost");

        if (args.length < 1) return false;

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player && !sender.hasPermission("bb.reload"))
                sender.sendMessage(ChatColor.RED + "You do not have the §4bb.reload §cpermission!");
            else {
                bb.reloadBB();
                sender.sendMessage(ChatColor.GREEN + "Reloaded BlockBoost Plugin! Use " +
                        ChatColor.YELLOW + "/bb list" + ChatColor.GREEN + " to display active" +
                        "Boost Blocks!");
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("info"))
            return Bukkit.dispatchCommand(sender, "version blockboost");

        else if (args[0].equalsIgnoreCase("list")) {
            // try to get a specific block to list
            if (args.length == 2) {
                BB type;

                try {
                    type = BB.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "BoostBlock " + ChatColor.DARK_RED +
                            args[1] + ChatColor.RED + " does not exist!");
                    return false;
                }

                List<String> list = new ArrayList<>();
                switch (type) {
                    case BOUNCE:
                    case SPEED_ADDITION:
                    case SPEED_MULTIPLIER:
                    case POTION:
                        list.addAll(bb.getBlockManager().getBlockList(type));
                        break;
                    case SPEED:
                        list.addAll(bb.getBlockManager().getBlockList(BB.SPEED_ADDITION));
                        list.addAll(bb.getBlockManager().getBlockList(BB.SPEED_MULTIPLIER));
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + args[1] + " is not registered as a " +
                                "Boost Block type!");
                        return true;
                }

                if (list.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "No matching Boost Blocks found!");
                    return true;
                }

                list.forEach(sender::sendMessage);
            }
            // Display all active boost blocks
            else {
                List<String> list = new ArrayList<>();
                for (BB type : BB.values())
                    list.addAll(bb.getBlockManager().getBlockList(type));

                list.forEach(sender::sendMessage);
            }
            return true;
        }

        return false;
    }
}
