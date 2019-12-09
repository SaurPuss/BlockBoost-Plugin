package me.saurpuss.blockboost.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceSpeed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            Player player = Bukkit.getPlayer(args[0]);
            float speed = 0.2f;
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player " + args[0] + " is not online!");
                return true;
            }

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
}
