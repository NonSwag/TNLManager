package net.nonswag.tnl.manager;

import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.utils.PluginUpdate;
import net.nonswag.tnl.manager.commands.PluginCommand;
import net.nonswag.tnl.manager.listener.CommandListener;
import net.nonswag.tnl.manager.tabcompleter.PluginCommandTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginManager extends JavaPlugin {

    @Override
    public void onEnable() {
        if (net.nonswag.tnl.manager.api.plugin.PluginManager.getInstance().getPlugin("TNLListener") != null) {
            try {
                new PluginUpdate(this).downloadUpdate();
            } catch (Throwable ignored) {
            }
        }
        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommand("plugin", "tnl.manage", new PluginCommand(), new PluginCommandTabComplete());
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
    }
}
