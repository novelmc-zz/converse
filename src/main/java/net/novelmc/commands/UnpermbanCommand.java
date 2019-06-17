package net.novelmc.commands;

import net.novelmc.permban.Permban;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnpermbanCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("converse.unpermban"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1)
        {
            return false;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        if (Permban.removePermban(offlinePlayer))
        {
            Util.action(sender, "Unbanning " + offlinePlayer.getName());
            return true;
        }
        else
        {
            sender.sendMessage(ChatColor.GRAY + "Converse could not find a permanent ban under that name.");
        }
        return true;
    }
}
