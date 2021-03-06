package net.nonswag.tnl.manager.completer;

import net.nonswag.tnl.manager.api.plugin.PluginManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginCommandTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (sender.hasPermission("tnl.manage")) {
            if (args.length <= 1) {
                suggestions.add("list");
                suggestions.add("info");
                suggestions.add("enable");
                suggestions.add("disable");
                suggestions.add("load");
                suggestions.add("reload");
                suggestions.add("unload");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("info")
                        || args[0].equalsIgnoreCase("enable")
                        || args[0].equalsIgnoreCase("disable")
                        || args[0].equalsIgnoreCase("reload")
                        || args[0].equalsIgnoreCase("unload")) {
                    suggestions.addAll(PluginManager.getInstance().getPlugins(false));
                } else if (args[0].equalsIgnoreCase("load")) {
                    for (File file : new File("plugins").listFiles()) {
                        if (file.getName().toLowerCase().endsWith(".jar")) {
                            if (PluginManager.getInstance().getPlugin(file.getName().replace(".jar", "")) == null) {
                                suggestions.add(file.getName());
                            }
                        }
                    }
                }
            }
            if (!suggestions.isEmpty() && args.length != 0) {
                suggestions.removeIf(suggestion -> !suggestion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
            }
        }
        return suggestions;
    }
}
