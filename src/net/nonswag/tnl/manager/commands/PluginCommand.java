package net.nonswag.tnl.manager.commands;

import net.nonswag.tnl.manager.PluginManager;
import net.nonswag.tnl.manager.utils.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "list":
                        List<String> pluginList = new ArrayList<>();
                        String name;
                        for (Plugin all : Bukkit.getPluginManager().getPlugins()) {
                            name = all.getName();
                            try {
                                if (all.getDescription().getAPIVersion() == null) {
                                    name = name + "*";
                                }
                            } catch (Throwable ignored) {
                            }
                            if (all.isEnabled()) {
                                pluginList.add("§a" + name);
                            } else {
                                pluginList.add("§c" + name);
                            }
                        }
                        sender.sendMessage(PluginManager.getPrefix() + " §7Plugins §8(§6" + pluginList.size() + "§8)§8: " + String.join("§8, §r", pluginList));
                        return true;
                    case "info":
                        if (args.length >= 2) {
                            Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
                            if (plugin != null) {
                                sender.sendMessage(PluginManager.getPrefix() + " §7Name§8: §6" + plugin.getName());
                                if (plugin.getDescription().getPrefix() != null) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Prefix§8: §6" + plugin.getDescription().getPrefix());
                                } else {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Prefix§8: §7-§8/§7-");
                                }
                                sender.sendMessage(PluginManager.getPrefix() + " §7Version§8: §6" + plugin.getDescription().getVersion());
                                sender.sendMessage(PluginManager.getPrefix() + " §7Enabled§8: §6" + plugin.isEnabled());
                                if (plugin.getDescription().getDescription() != null) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Description§8: §6" + plugin.getDescription().getDescription());
                                } else {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Description§8: §7-§8/§7-");
                                }
                                if (plugin.getDescription().getAuthors().size() > 0) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Authors§8: §6" + String.join("§8, §6", plugin.getDescription().getAuthors()));
                                } else {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Authors§8: §7-§8/§7-");
                                }
                                if (plugin.getDescription().getWebsite() != null) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Website§8: §6" + plugin.getDescription().getWebsite());
                                } else {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Website§8: §7-§8/§7-");
                                }
                                if (plugin.getDescription().getDepend().size() > 0) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Depends§8: §6" + String.join("§8, §6", plugin.getDescription().getDepend()));
                                } else {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Depends§8: §7-§8/§7-");
                                }
                                sender.sendMessage(PluginManager.getPrefix() + " §7Main§8: §6" + plugin.getDescription().getMain());
                                sender.sendMessage(PluginManager.getPrefix() + " §7DataFolder§8: §6" + plugin.getDataFolder().getAbsolutePath());
                                if (Bukkit.getVersion().contains("1.13")
                                        || Bukkit.getVersion().contains("1.14")
                                        || Bukkit.getVersion().contains("1.15")) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7API§8: §6" + plugin.getDescription().getAPIVersion());
                                }
                                sender.sendMessage(PluginManager.getPrefix() + " §7Logger§8: §6" + plugin.getLogger().getName());
                                if (plugin.getDescription().getCommands().size() > 0) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Commands§8: §6" + String.join("§8, §6", plugin.getDescription().getCommands().keySet()));
                                } else {
                                    sender.sendMessage(PluginManager.getPrefix() + " §7Commands§8: §7-§8/§7-");
                                }
                                return true;
                            } else {
                                sender.sendMessage(PluginManager.getPrefix() + " §cThe Plugin §4" + args[1] + "§c was not loaded before");
                            }
                        } else {
                            sender.sendMessage(PluginManager.getPrefix() + " §c/plugin info §8[§6Plugin§8]");
                        }
                        return false;
                    case "enable":
                        if (args.length >= 2) {
                            Plugin plugin = PluginUtil.getPluginByName(args[1], true);
                            if (plugin != null) {
                                if (!plugin.isEnabled()) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §aUnloading Plugin §6" + plugin.getName());
                                    PluginUtil.enable(plugin);
                                    sender.sendMessage(PluginManager.getPrefix() + " §aSuccessfully unloaded Plugin §6" +
                                            plugin.getName());
                                } else {
                                    sender.sendMessage(PluginManager.getPrefix() + " §cThe Plugin §4" +
                                            plugin.getName() + "§c is already enabled");
                                }
                            } else {
                                sender.sendMessage(PluginManager.getPrefix() + " §cThe Plugin §4" + args[1] + "§c was not loaded before");
                            }
                        } else {
                            sender.sendMessage(PluginManager.getPrefix() + " §c/plugin enable §8[§6Plugin§8]");
                        }
                        break;
                    case "disable":
                        if (args.length >= 2) {
                            Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
                            if (plugin != null) {
                                sender.sendMessage(PluginManager.getPrefix() + " §aDisabling Plugin §6" + plugin.getName());
                                PluginUtil.disable(plugin);
                                sender.sendMessage(PluginManager.getPrefix() + " §aSuccessfully disabled Plugin §6" +
                                        plugin.getName());
                            } else {
                                sender.sendMessage(PluginManager.getPrefix() + " §cThe Plugin §4" + args[1] + "§c was not loaded before");
                            }
                        } else {
                            sender.sendMessage(PluginManager.getPrefix() + " §c/plugin enable §8[§6Plugin§8]");
                        }
                        break;
                    case "load":
                        if (args.length >= 2) {
                            try {
                                File file = new File(PluginUtil.Folder.BUKKIT.getDir() + "/" + args[1]);
                                if (file.exists()) {
                                    if(file.isFile()) {
                                        Plugin plugin = PluginUtil.getPluginByName(args[1], false);
                                        if (plugin == null || (plugin != null && !plugin.isEnabled())) {
                                            Plugin pl = PluginUtil.getPluginByName(args[1].replace(".jar", ""), true);
                                            if (pl == null) {
                                                sender.sendMessage(PluginManager.getPrefix() + " §aLoading plugin §6" + args[1].replace(".jar", ""));
                                                PluginUtil.load(args[1].replace(".jar", ""), PluginUtil.Folder.BUKKIT);
                                                sender.sendMessage(PluginManager.getPrefix() + " §aSuccessfully loaded plugin §6" + args[1].replace(".jar", ""));
                                            } else {
                                                sender.sendMessage(PluginManager.getPrefix() + " §cThe plugin §4" + args[1].replace(".jar", "") + "§c is already loaded");
                                            }
                                        } else {
                                            sender.sendMessage(PluginManager.getPrefix() + " §cThe plugin §4" + plugin.getName() + "§c is already enabled");
                                        }
                                    } else {
                                        sender.sendMessage(PluginManager.getPrefix() + " §cThe file §4" + file.getName() + "§c is not a §8(§4jar§8)§c file");
                                    }
                                } else {
                                    sender.sendMessage(PluginManager.getPrefix() + " §cThe file §4" + args[1] + "§c doesn't exist");
                                }
                            } catch (Throwable t) {
                                sender.sendMessage(PluginManager.getPrefix() + " §cAn error has occurred while loading plugin §4" + args[1]);
                                t.printStackTrace();
                            }
                        } else {
                            sender.sendMessage(PluginManager.getPrefix() + " §c/plugin load §8[§6File Name§8]");
                        }
                        break;
                    case "reload":
                        if (args.length >= 2) {
                            Plugin plugin = PluginUtil.getPluginByName(args[1], true);
                            if (plugin != null) {
                                sender.sendMessage(PluginManager.getPrefix() + " §aReloading plugin §6" + plugin.getName());
                                try {
                                    PluginUtil.reload(plugin, PluginUtil.Folder.BUKKIT);
                                    sender.sendMessage(PluginManager.getPrefix() + " §aSuccessfully reloaded plugin §6" + plugin.getName());
                                } catch (Throwable t) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §cAn error has occurred while reloading plugin §4" + plugin.getName());
                                    t.printStackTrace();
                                }
                            } else {
                                sender.sendMessage(PluginManager.getPrefix() + " §cThe plugin §4" + args[1] + "§c was not loaded before");
                            }
                        } else {
                            sender.sendMessage(PluginManager.getPrefix() + " §c/plugin reload §8[§6Plugin§8]");
                        }
                        break;
                    case "unload":
                        if (args.length >= 2) {
                            Plugin plugin = PluginUtil.getPluginByName(args[1], true);
                            if (plugin != null) {
                                sender.sendMessage(PluginManager.getPrefix() + " §aUnloading Plugin §6" + plugin.getName());
                                try {
                                    PluginUtil.unload(plugin);
                                    sender.sendMessage(PluginManager.getPrefix() + " §aSuccessfully unloaded Plugin §6" + plugin.getName());
                                } catch (Exception e) {
                                    sender.sendMessage(PluginManager.getPrefix() + " §cAn error has occurred while unloading plugin §4" + plugin.getName());
                                    e.printStackTrace();
                                }
                            } else {
                                sender.sendMessage(PluginManager.getPrefix() + " §cThe Plugin §4" + args[1] + "§c was not loaded before");
                            }
                        } else {
                            sender.sendMessage(PluginManager.getPrefix() + " §c/plugin unload §8[§6Plugin§8]");
                        }
                        break;
                }
            } else {
                sender.sendMessage(PluginManager.getPrefix() + " §c/plugin list");
                sender.sendMessage(PluginManager.getPrefix() + " §c/plugin info §8[§6Plugin§8]");
                sender.sendMessage(PluginManager.getPrefix() + " §c/plugin enable §8[§6Plugin§8]");
                sender.sendMessage(PluginManager.getPrefix() + " §c/plugin disable §8[§6Plugin§8]");
                sender.sendMessage(PluginManager.getPrefix() + " §c/plugin load §8[§6File Name§8]");
                sender.sendMessage(PluginManager.getPrefix() + " §c/plugin reload §8[§6Plugin§8]");
                sender.sendMessage(PluginManager.getPrefix() + " §c/plugin unload §8[§6Plugin§8]");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }
}
