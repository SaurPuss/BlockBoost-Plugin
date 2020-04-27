package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base plugin command for reloading the plugin as well as list active Boost Blocks for users
 * with appropriate bb permission nodes.
 */
public class BlockBoostCommand implements CommandExecutor {

    private final BlockBoost bb;
    private List<SubCommand> commands = new ArrayList<>();

    public BlockBoostCommand(BlockBoost plugin) {
        bb = plugin;

        commands.add(new InfoCommand());
        commands.add(new ExampleConfigCommand(bb));
        commands.add(new ListBlocksCommand(bb));
        commands.add(new ReloadCommand(bb));
        // TODO CreateBlockCommand - Use conversation API?
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bb.admin"))
            return Bukkit.dispatchCommand(sender, "version blockboost");
        else if (args.length == 0)
            return false;

        SubCommand target = getSubCommand(args[0]);
        if  (target == null) {
            sender.sendMessage(ChatColor.RED + args[0] + " is not a valid argument");
            return false;
        }

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        return target.onCommand(sender, subArgs);
    }

    private SubCommand getSubCommand(String name) {
        for (SubCommand subCommand : commands) {
            if (subCommand.name().equalsIgnoreCase(name))
                return subCommand;

            String[] aliases;
            int length = (aliases = subCommand.aliases()).length;
            for (int i = 0; i < length; ++i) {
                if (name.equalsIgnoreCase(aliases[i]))
                    return subCommand;
            }
        }

        return null;
    }
}
