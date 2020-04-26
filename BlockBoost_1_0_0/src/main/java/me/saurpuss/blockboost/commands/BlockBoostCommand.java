package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base plugin command for reloading the plugin as well as list active Boost Blocks for users
 * with appropriate bb permission nodes.
 */
public class BlockBoostCommand implements CommandExecutor {

    // TODO add single blocks in game -> conversation right click target block maybe?

    private final BlockBoost bb;
    private List<SubCommand> commands = new ArrayList<>();

    public BlockBoostCommand(BlockBoost plugin) {
        bb = plugin;

        commands.add(new ReloadCommand(bb));
        commands.add(new ExampleConfigCommand(bb));
        commands.add(new ListBlocksCommand(bb));
        commands.add(new SetSpeedCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bb.admin") ||
                (args.length >= 1 && args[0].equalsIgnoreCase("info")))
            return Bukkit.dispatchCommand(sender, "version blockboost");

        else if (args.length == 0)
            return false;


        ArrayList<String> list = new ArrayList<>(Arrays.asList(args));
        Player player;
        if (sender instanceof Player) {
            // Determine player to perform this on
            player = Bukkit.getPlayer(list.get(0));
            if (player == null)
                player = (Player) sender; // first arg is not a player
            else
                list.remove(0); // remove player arg
        } else {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                SubCommand sub = getSubCommand(args[0]);
                if (sub == null)
                    return false;

                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                return sub.onCommand(Bukkit.getConsoleSender(), subArgs);
            } else
                list.remove(0); // remove player arg
        }

        // Get SubCommand
        SubCommand target = getSubCommand(list.get(0));
        if (target == null)
            return false;
        list.remove(0); // remove subCommand arg

        return target.onCommand(player, (String[]) list.toArray());
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
