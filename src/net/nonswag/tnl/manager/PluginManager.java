package net.nonswag.tnl.manager;

import net.nonswag.tnl.manager.commands.PluginCommand;
import net.nonswag.tnl.manager.listener.CommandListener;
import net.nonswag.tnl.manager.tabcompleter.PluginCommandTabComplete;
import net.nonswag.tnl.manager.utils.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public class PluginManager extends JavaPlugin {

    @Nullable private static Plugin plugin;
    @Nullable private static String managerFolder;
    @Nullable private static String prefix;

    @Override
    public void onEnable() {
        try {
            setPlugin(this);
            if (PluginUtil.getPlugin("TNLListener") != null) {
                try {
                    setPrefix(net.nonswag.tnl.listener.NMSMain.getPrefix());
                } catch (Throwable ignored) {
                    setPrefix("§8[§f§lTNL§8]");
                }
            } else {
                setPrefix("§8[§f§lTNL§8]");
            }
            managerFolder = getPlugin().getDataFolder().getAbsolutePath() + "/plugins";
            this.getCommand("plugin").setExecutor(new PluginCommand());
            this.getCommand("plugin").setPermission("tnl.manage");
            this.getCommand("plugin").setPermissionMessage(PluginManager.getPrefix() + " §cYou have no Rights §8(§4tnl.manage§8)");
            this.getCommand("plugin").setTabCompleter(new PluginCommandTabComplete());

            Bukkit.getPluginManager().registerEvents(new CommandListener(), this);

            if(!PluginUtil.Folder.MANAGER.getFile().getParentFile().exists()) {
                PluginUtil.Folder.MANAGER.getFile().getParentFile().mkdir();
            }
            if(!PluginUtil.Folder.MANAGER.getFile().exists()) {
                PluginUtil.Folder.MANAGER.getFile().mkdir();
            }
            PluginUtil.reloadAll(PluginUtil.Folder.MANAGER);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Nullable
    public static Plugin getPlugin() {
        return plugin;
    }

    @Nullable
    public static String getManagerFolder() {
        return managerFolder;
    }

    @Nullable
    public static String getPrefix() {
        return prefix;
    }

    public static void setPlugin(@Nullable Plugin plugin) {
        PluginManager.plugin = plugin;
    }

    public static void setManagerFolder(@Nullable String managerFolder) {
        PluginManager.managerFolder = managerFolder;
    }

    public static void setPrefix(@Nullable String prefix) {
        PluginManager.prefix = prefix;
    }
}
