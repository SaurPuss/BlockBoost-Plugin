package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.util.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * In case of emergency fire command! This will manually reset a player's walk speed.
 */
public class SetSpeedCommand extends SubCommand {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        // TODO check if the name & alias work out well
        if (args.length >= 1) {
            Player player = Bukkit.getPlayer(args[0]);
            float speed = 0.2f;
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player " + args[0] + " is not online!");
                return true;
            }

            // TODO check if speed is still saved in a list
            // TODO reset cooldown to now?

            if (args.length == 2) {
                try {
                    speed = Float.parseFloat(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + args[1] + " is not a valid number!");
                    return true;
                }

                if (speed < -1.0 || speed > 1.0) {
                    sender.sendMessage(ChatColor.RED + args[1] + " is not a valid walkSpeed " +
                            "number! Choose a number between -1.0 and 1.0!");
                    return true;
                }
            }

            player.setWalkSpeed(speed);
            sender.sendMessage(ChatColor.GREEN + "WalkSpeed for player: " + player.getName() +
                    " set to " + speed);
            return true;
        }

        return false;
    }

    @Override
    public String name() {
        return "speed";
    }

    @Override
    public String[] getUsage() {
        return new String[] {"forcespeed", "setspeed"};
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
