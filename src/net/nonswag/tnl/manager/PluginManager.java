package net.nonswag.tnl.manager;

import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.manager.commands.PluginCommand;
import net.nonswag.tnl.manager.listener.CommandListener;
import net.nonswag.tnl.manager.tabcompleter.PluginCommandTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginManager extends JavaPlugin {

    @Override
    public void onEnable() {
        if (net.nonswag.tnl.manager.api.plugin.PluginManager.getInstance().getPlugin("TNLListener") != null) {
            if (Settings.AUTO_UPDATER.getValue()) {
                new PluginUpdate(this).downloadUpdate();
            }
        }
        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommand("plugin", "tnl.manage", new PluginCommand(), new PluginCommandTabComplete());
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
    }
}
