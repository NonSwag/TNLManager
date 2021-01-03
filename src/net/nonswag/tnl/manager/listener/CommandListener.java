package net.nonswag.tnl.manager.listener;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Arrays;
import java.util.List;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(ServerCommandEvent event) {
        if (onCommand(event.getSender(), event.getCommand().split(" ")[0])) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (onCommand(event.getPlayer(), event.getMessage().split(" ")[0].replaceFirst("/", ""))) {
            event.setCancelled(true);
        }
    }

    private boolean onCommand(CommandSender sender, String command) {
        if (command.equalsIgnoreCase("pl") || command.equalsIgnoreCase("plugins")) {
            Bukkit.dispatchCommand(sender, "plugin list");
            return true;
        }
        return false;
    }
}
