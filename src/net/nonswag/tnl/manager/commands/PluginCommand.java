package net.nonswag.tnl.manager.commands;

import net.nonswag.tnl.listener.api.item.v1_15.R1.NMSItem;
import net.nonswag.tnl.listener.api.message.ChatComponent;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.manager.Manager;
import net.nonswag.tnl.manager.api.plugin.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginCommand implements CommandExecutor {

    private static final ItemStack placeholder1 = new NMSItem(Material.GRAY_STAINED_GLASS_PANE).setName("§7-§8/§7-").build();
    private static final ItemStack placeholder2 = new NMSItem(Material.WHITE_STAINED_GLASS_PANE).setName("§7-§8/§7-").build();

    public static void sendPlugins(@Nonnull CommandSender viewer) {
        if (Manager.getInstance().isEveryoneCanSeePlugins() || viewer.hasPermission("tnl.manage")) {
            if (!Manager.getInstance().isPluginsGUI() || !(viewer instanceof Player)) {
                List<String> pluginList = new ArrayList<>();
                for (Plugin all : Bukkit.getPluginManager().getPlugins()) {
                    pluginList.add(PluginManager.getInstance().getName(all, false));
                }
                viewer.sendMessage(ChatComponent.getText("%prefix% §7Plugins §8(§6" + pluginList.size() + "§8)§8: " + String.join("§8, §r", pluginList)));
            } else {
                Player player = (Player) viewer;
                Inventory inventory = Bukkit.createInventory(null, 54, "§8» §6§lPlugin§e§lList");
                inventory.setItem(0, placeholder2);
                inventory.setItem(8, placeholder2);
                inventory.setItem(9, placeholder1);
                inventory.setItem(17, placeholder1);
                inventory.setItem(18, placeholder1);
                inventory.setItem(26, placeholder1);
                inventory.setItem(27, placeholder1);
                inventory.setItem(35, placeholder1);
                inventory.setItem(36, placeholder1);
                inventory.setItem(44, placeholder1);
                inventory.setItem(inventory.getSize() - 9, placeholder2);
                inventory.setItem(inventory.getSize() - 1, placeholder2);
                for (int i = 1; i <= 7; i++) {
                    inventory.setItem(i, placeholder1);
                }
                for (int i = inventory.getSize() - 8; i <= inventory.getSize() - 2; i++) {
                    inventory.setItem(i, placeholder1);
                }
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    List<String> authors = plugin.getDescription().getAuthors();
                    if (authors.size() == 1) {
                        inventory.addItem(new NMSItem(Material.BOOK).setName("§8* §r" + PluginManager.getInstance().getName(plugin, true)).setLore("§8* §7Author§8: §6" + authors.get(0)).build());
                    } else {
                        List<String> lore = new ArrayList<>();
                        lore.add("§8* §7Authors§8: §6" + authors.size());
                        for (String s : authors) {
                            lore.add("§6" + s);
                        }
                        inventory.addItem(new NMSItem(Material.BOOK).setName("§8* §r" + PluginManager.getInstance().getName(plugin, true)).setLore(lore).build());
                    }
                }
                player.openInventory(inventory);
            }
        } else {
            viewer.sendMessage(Message.NO_PERMISSION_EN.getText(new Placeholder("permission", "tnl.manage")));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) {
                sendPlugins(sender);
            } else if (args[0].equalsIgnoreCase("info")) {
                if (args.length >= 2) {
                    Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
                    if (plugin != null) {
                        sender.sendMessage(ChatComponent.getText("%prefix% §7Name§8: §6" + plugin.getName()));
                        if (plugin.getDescription().getPrefix() != null) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Prefix§8: §6" + plugin.getDescription().getPrefix()));
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Prefix§8: §7-§8/§7-"));
                        }
                        sender.sendMessage(ChatComponent.getText("%prefix% §7Version§8: §6" + plugin.getDescription().getVersion()));
                        sender.sendMessage(ChatComponent.getText("%prefix% §7Enabled§8: §6" + plugin.isEnabled()));
                        if (plugin.getDescription().getDescription() != null) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Description§8: §6" + plugin.getDescription().getDescription()));
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Description§8: §7-§8/§7-"));
                        }
                        if (plugin.getDescription().getAuthors().size() > 0) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Authors§8: §6" + String.join("§8, §6", plugin.getDescription().getAuthors())));
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Authors§8: §7-§8/§7-"));
                        }
                        if (plugin.getDescription().getWebsite() != null) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Website§8: §6" + plugin.getDescription().getWebsite()));
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Website§8: §7-§8/§7-"));
                        }
                        if (plugin.getDescription().getDepend().size() > 0) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Depends§8: §6" + String.join("§8, §6", plugin.getDescription().getDepend())));
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Depends§8: §7-§8/§7-"));
                        }
                        sender.sendMessage(ChatComponent.getText("%prefix% §7Main§8: §6" + plugin.getDescription().getMain()));
                        sender.sendMessage(ChatComponent.getText("%prefix% §7DataFolder§8: §6" + plugin.getDataFolder().getAbsolutePath()));
                        if (Bukkit.getVersion().contains("1.13")
                                || Bukkit.getVersion().contains("1.14")
                                || Bukkit.getVersion().contains("1.15")) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7API§8: §6" + plugin.getDescription().getAPIVersion()));
                        }
                        sender.sendMessage(ChatComponent.getText("%prefix% §7Logger§8: §6" + plugin.getLogger().getName()));
                        if (plugin.getDescription().getCommands().size() > 0) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Commands§8: §6" + String.join("§8, §6", plugin.getDescription().getCommands().keySet())));
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix% §7Commands§8: §7-§8/§7-"));
                        }
                    } else {
                        sender.sendMessage(ChatComponent.getText("%prefix% §cThe Plugin §4" + args[1] + "§c was not loaded before"));
                    }
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin info §8[§6Plugin§8]"));
                }
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (args.length >= 2) {
                    Plugin plugin = PluginManager.getInstance().getPlugin(args[1]);
                    if (plugin != null) {
                        if (!plugin.isEnabled()) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §aUnloading Plugin §6" + plugin.getName()));
                            PluginManager.getInstance().enable(plugin);
                            sender.sendMessage(ChatComponent.getText("%prefix% §aSuccessfully unloaded Plugin §6" + plugin.getName()));
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix% §cThe Plugin §4" + plugin.getName() + "§c is already enabled"));
                        }
                    } else {
                        sender.sendMessage(ChatComponent.getText("%prefix% §cThe Plugin §4" + args[1] + "§c was not loaded before"));
                    }
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin enable §8[§6Plugin§8]"));
                }
            } else if (args[0].equalsIgnoreCase("disable")) {
                if (args.length >= 2) {
                    Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
                    if (plugin != null) {
                        sender.sendMessage(ChatComponent.getText("%prefix% §aDisabling Plugin §6" + plugin.getName()));
                        PluginManager.getInstance().disable(plugin);
                        sender.sendMessage(ChatComponent.getText("%prefix% §aSuccessfully disabled Plugin §6" + plugin.getName()));
                    } else {
                        sender.sendMessage(ChatComponent.getText("%prefix% §cThe Plugin §4" + args[1] + "§c was not loaded before"));
                    }
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin enable §8[§6Plugin§8]"));
                }
            } else if (args[0].equalsIgnoreCase("load")) {
                if (args.length >= 2) {
                    try {
                        File file = new File("plugins/" + args[1]);
                        if (file.exists()) {
                            if (file.isFile()) {
                                Plugin plugin = PluginManager.getInstance().getPlugin(args[1]);
                                if (plugin == null || (plugin != null && !plugin.isEnabled())) {
                                    Plugin pl = PluginManager.getInstance().getPlugin(args[1].replace(".jar", ""));
                                    if (pl == null) {
                                        sender.sendMessage(ChatComponent.getText("%prefix% §aLoading plugin §6" + args[1].replace(".jar", "")));
                                        try {
                                            PluginManager.getInstance().load(args[1].replace(".jar", ""));
                                            sender.sendMessage(ChatComponent.getText("%prefix% §aSuccessfully loaded plugin §6" + args[1].replace(".jar", "")));
                                        } catch (InvalidDescriptionException | InvalidPluginException e) {
                                            sender.sendMessage(ChatComponent.getText("%prefix% §cFailed to load plugin §4" + args[1].replace(".jar", "")));
                                            e.printStackTrace();
                                        }
                                    } else {
                                        sender.sendMessage(ChatComponent.getText("%prefix% §cThe plugin §4" + args[1].replace(".jar", "") + "§c is already loaded"));
                                    }
                                } else {
                                    sender.sendMessage(ChatComponent.getText("%prefix% §cThe plugin §4" + plugin.getName() + "§c is already enabled"));
                                }
                            } else {
                                sender.sendMessage(ChatComponent.getText("%prefix% §cThe file §4" + file.getName() + "§c is not a §8(§4jar§8)§c file"));
                            }
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix% §cThe file §4" + args[1] + "§c doesn't exist"));
                        }
                    } catch (Exception e) {
                        sender.sendMessage(ChatComponent.getText("%prefix% §cAn error has occurred while loading plugin §4" + args[1]));
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin load §8[§6File Name§8]"));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (args.length >= 2) {
                    Plugin plugin = PluginManager.getInstance().getPlugin(args[1]);
                    if (plugin != null) {
                        sender.sendMessage(ChatComponent.getText("%prefix% §aReloading plugin §6" + plugin.getName()));
                        try {
                            PluginManager.getInstance().reload(plugin);
                            sender.sendMessage(ChatComponent.getText("%prefix% §aSuccessfully reloaded plugin §6" + plugin.getName()));
                        } catch (Exception e) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §cAn error has occurred while reloading plugin §4" + plugin.getName()));
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(ChatComponent.getText("%prefix% §cThe plugin §4" + args[1] + "§c was not loaded before"));
                    }
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin reload §8[§6Plugin§8]"));
                }
            } else if (args[0].equalsIgnoreCase("unload")) {
                if (args.length >= 2) {
                    Plugin plugin = PluginManager.getInstance().getPlugin(args[1]);
                    if (plugin != null) {
                        sender.sendMessage(ChatComponent.getText("%prefix% §aUnloading Plugin §6" + plugin.getName()));
                        try {
                            PluginManager.getInstance().unload(plugin);
                            sender.sendMessage(ChatComponent.getText("%prefix% §aSuccessfully unloaded Plugin §6" + plugin.getName()));
                        } catch (Exception e) {
                            sender.sendMessage(ChatComponent.getText("%prefix% §cAn error has occurred while unloading plugin §4" + plugin.getName()));
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(ChatComponent.getText("%prefix% §cThe Plugin §4" + args[1] + "§c was not loaded before"));
                    }
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin unload §8[§6Plugin§8]"));
                }
            } else {
                sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin list"));
                sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin info §8[§6Plugin§8]"));
                sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin enable §8[§6Plugin§8]"));
                sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin disable §8[§6Plugin§8]"));
                sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin load §8[§6File Name§8]"));
                sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin reload §8[§6Plugin§8]"));
                sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin unload §8[§6Plugin§8]"));
            }
        } else {
            sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin list"));
            sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin info §8[§6Plugin§8]"));
            sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin enable §8[§6Plugin§8]"));
            sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin disable §8[§6Plugin§8]"));
            sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin load §8[§6File Name§8]"));
            sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin reload §8[§6Plugin§8]"));
            sender.sendMessage(ChatComponent.getText("%prefix% §c/plugin unload §8[§6Plugin§8]"));
        }
        return false;
    }
}
