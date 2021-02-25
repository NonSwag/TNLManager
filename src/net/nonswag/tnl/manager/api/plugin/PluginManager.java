package net.nonswag.tnl.manager.api.plugin;

import java.lang.reflect.Field;

import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;

import java.util.*;
import java.net.URLClassLoader;

import org.bukkit.plugin.*;
import org.bukkit.command.SimpleCommandMap;

import java.io.File;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PluginManager implements Manager {

    @Nonnull
    private static final PluginManager instance = new PluginManager();

    @Nonnull
    public static PluginManager getInstance() {
        return instance;
    }

    protected PluginManager() {
    }

    @Override
    public void enableAll() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            enable(plugin);
        }
    }

    @Override
    public void enable(@Nonnull Plugin plugin) {
        if (!plugin.isEnabled()) {
            Bukkit.getPluginManager().enablePlugin(plugin);
        }
    }

    @Override
    public void disableAll() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            disable(plugin);
        }
    }

    @Override
    public void unloadAll() {
        for (Plugin plugin : getPlugins()) {
            unload(plugin);
        }
    }

    @Override
    public void disable(@Nonnull Plugin plugin) {
        if (plugin.isEnabled()) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }

    @Override
    public void loadAll() throws InvalidDescriptionException, InvalidPluginException {
        for (Plugin plugin : getPlugins()) {
            load("plugins/" + getName(plugin, false));
        }
    }

    @Nonnull
    @Override
    public Plugin[] getPlugins() {
        return Bukkit.getPluginManager().getPlugins();
    }

    @Nonnull
    @Override
    public String getName(@Nonnull Plugin plugin, boolean includeVersion) {
        if (includeVersion) {
            return (plugin.isEnabled() ? "§a" : "§c") + plugin.getName() + " §8(§7" + getVersion(plugin) + (plugin.getDescription().getAPIVersion() == null ? "*" : "") + "§8)";
        } else {
            return plugin.getName();
        }
    }

    @Nullable
    @Override
    public Plugin getPlugin(@Nonnull String name) {
        return Bukkit.getPluginManager().getPlugin(name);
    }

    @Nonnull
    @Override
    public List<String> getPlugins(boolean includeVersion) {
        List<String> plugins = new ArrayList<>();
        for (Plugin plugin : getPlugins()) {
            plugins.add(getName(plugin, false));
        }
        return plugins;
    }

    @Nonnull
    @Override
    public String getVersion(@Nonnull Plugin plugin) {
        return plugin.getDescription().getVersion();
    }

    @Override
    public void load(@Nonnull String name) throws InvalidDescriptionException, InvalidPluginException {
        Plugin plugin = Bukkit.getPluginManager().loadPlugin(new File(name.endsWith(".jar") ? name : name + ".jar"));
        if (plugin != null) {
            Bukkit.getPluginManager().enablePlugin(plugin);
        }
    }

    @Override
    public void reload(@Nonnull Plugin plugin) throws InvalidDescriptionException, InvalidPluginException {
        if (plugin.isEnabled()) {
            unload(plugin);
            disable(plugin);
            load("plugins/" + getName(plugin, false));
            for (Plugin all : getPlugins()) {
                if (!all.equals(plugin)) {
                    if (all.getDescription().getDepend().contains(plugin.getName()) || all.getDescription().getSoftDepend().contains(plugin.getName())) {
                        unload(all);
                        disable(all);
                        load("plugins/" + getName(all, false));
                    }
                }
            }
        }
    }

    @Override
    public void reloadAll() throws InvalidDescriptionException, InvalidPluginException {
        for (Plugin plugin : getPlugins()) {
            reload(plugin);
        }
    }

    @Override
    public void unload(@Nonnull Plugin plugin) {
        String name = getName(plugin, false);
        org.bukkit.plugin.PluginManager pluginManager = Bukkit.getPluginManager();
        SimpleCommandMap commandMap = null;
        Plugin[] plugins = getPlugins();
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        disable(plugin);
        try {
            Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
            lookupNamesField.setAccessible(true);
            names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);
        } catch (Exception ignored) {
        }
        try {
            Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
            listenersField.setAccessible(true);
            listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
        } catch (Exception ignored) {
        }
        try {
            Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
        } catch (Exception ignored) {
        }
        try {
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            commands = (Map<String, Command>) knownCommandsField.get(commandMap);
        } catch (Exception ignored) {
        }
        disable(plugin);
        if (names != null) {
            names.remove(name);
        }
        if (listeners != null) {
            for (SortedSet<RegisteredListener> set : listeners.values()) {
                set.removeIf(value -> value.getPlugin() == plugin);
            }
        }
        if (commandMap != null) {
            Iterator<Map.Entry<String, Command>> it2 = commands.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Command> entry = it2.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand command = (PluginCommand) entry.getValue();
                    if (command.getPlugin() == plugin) {
                        command.unregister(commandMap);
                        it2.remove();
                    }
                }
            }
        }
        ClassLoader cl = plugin.getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            try {
                Field pluginField = cl.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(cl, null);
            } catch (Exception ignored) {
            }
            try {
                Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
                pluginInitField.setAccessible(true);
                pluginInitField.set(cl, null);
            } catch (Exception ignored) {
            }
            try {
                ((URLClassLoader) cl).close();
            } catch (Exception ignored) {
            }
        }
        plugin.getName();
    }
}
