package net.nonswag.tnl.manager.listener;

import net.nonswag.tnl.manager.commands.PluginCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import javax.annotation.Nonnull;

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

    private boolean onCommand(@Nonnull CommandSender sender, @Nonnull String command) {
        if (command.equalsIgnoreCase("pl") || command.equalsIgnoreCase("plugins")) {
            PluginCommand.sendPlugins(sender);
            return true;
        }
        return false;
    }
}
