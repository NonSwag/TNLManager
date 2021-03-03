package net.nonswag.tnl.manager;

import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.utils.PluginUpdate;
import net.nonswag.tnl.manager.commands.PluginCommand;
import net.nonswag.tnl.manager.listener.CommandListener;
import net.nonswag.tnl.manager.tabcompleter.PluginCommandTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public class PluginManager extends JavaPlugin {

    @Nullable private static String prefix;

    @Override
    public void onEnable() {
        if (net.nonswag.tnl.manager.api.plugin.PluginManager.getInstance().getPlugin("TNLListener") != null) {
            try {
                new PluginUpdate(this).downloadUpdate();
                setPrefix(TNLListener.getInstance().getPrefix());
            } catch (Throwable ignored) {
                setPrefix("§8[§f§lTNL§8]");
            }
        } else {
            setPrefix("§8[§f§lTNL§8]");
        }
        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommand("plugin", "tnl.manage", new PluginCommand(), new PluginCommandTabComplete());
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
    }

    @Nullable
    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(@Nullable String prefix) {
        net.nonswag.tnl.manager.PluginManager.prefix = prefix;
    }
}
