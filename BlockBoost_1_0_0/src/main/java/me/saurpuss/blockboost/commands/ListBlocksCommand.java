package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BB;
import me.saurpuss.blockboost.util.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ListBlocksCommand extends SubCommand {

    private BlockBoost bb;

    ListBlocksCommand(BlockBoost plugin) {
        bb = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {


        if (args[0].equalsIgnoreCase("type")) {
            StringJoiner joiner = new StringJoiner("§a, §2", ChatColor.GREEN +
                    "Available BlockBoost types: §2", "§a.");
            for (BB type : BB.values()) {
                joiner.add(type.toString());
            }

            sender.sendMessage(joiner.toString());
            return true;
        }

        List<AbstractBlock> list;
        if (args.length == 2) {
            BB type;

            try {
                type = BB.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "BoostBlock " + ChatColor.DARK_RED +
                        args[1] + ChatColor.RED + " does not exist!");
                return false;
            }

            switch (type) {
                case BOUNCE:
                case SPEED:
                case POTION:
                    list = new ArrayList<>(bb.getBlockManager().getBlockList(type));
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

            // TODO pagination
            list.forEach(block -> sender.sendMessage(block.toColorString()));
        }
        // Display all active boost blocks
        else {
            List<String> temp = new ArrayList<>();
            for (BB type : BB.values()) {
                // TODO pretty title separation
                temp.add(type.toString());
                list = new ArrayList<>(bb.getBlockManager().getBlockList(type));
                list.forEach(block -> temp.add(block.toColorString()));
            }

            // TODO pagination
            temp.forEach(sender::sendMessage);
        }




        return true;
    }

    @Override
    public String name() {
        return null;
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
