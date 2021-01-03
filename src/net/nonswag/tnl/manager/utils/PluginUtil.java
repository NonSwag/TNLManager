package net.nonswag.tnl.manager.utils;

import java.lang.reflect.Field;

import net.nonswag.tnl.manager.PluginManager;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;

import java.util.*;
import java.net.URLClassLoader;

import org.bukkit.plugin.*;
import org.bukkit.command.SimpleCommandMap;

import java.io.File;

import org.bukkit.Bukkit;

public class PluginUtil {

    public static void enable(Plugin plugin) {
        if (plugin != null && !plugin.isEnabled()) {
            Bukkit.getPluginManager().enablePlugin(plugin);
        }
    }

    public static void enableAll() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            enable(plugin);
        }
    }

    public static void disable(Plugin plugin) {
        if (plugin != null && plugin.isEnabled()) {
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public static void disableAll() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            disable(plugin);
        }
    }

    public static String getFormattedName(Plugin plugin, boolean includeVersions) {
        String pluginName = plugin.isEnabled() ? "§a" : "§c" + plugin.getName();
        if (includeVersions) {
            pluginName = pluginName + " (" + plugin.getDescription().getVersion() + ")";
        }
        return pluginName;
    }

    public static Plugin getPluginByName(String name, boolean unsafe) {
        if (!unsafe) {
            for (Plugin all : PluginUtil.getAll()) {
                if (all.getName().equalsIgnoreCase(name)) {
                    return all;
                }
            }
        } else {
            return Bukkit.getPluginManager().getPlugin(name);
        }
        return null;
    }

    public static Plugin getPlugin(String name) {
        return getPluginByName(name, false);
    }

    public static List<String> getPluginNames(boolean fullName) {
        List<String> plugins = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            plugins.add(fullName ? plugin.getDescription().getFullName() : plugin.getName());
        }
        return plugins;
    }

    public static String getPluginVersion(String name) {
        Plugin plugin = getPluginByName(name, true);
        if (plugin != null) {
            return plugin.getDescription().getVersion();
        }
        return null;
    }

    public static void load(Plugin plugin, Folder folder) throws Throwable {
        load(plugin.getName(), folder);
    }

    public static void load(String name, Folder folder) throws Throwable {
        Plugin target;
        File pluginDir = new File(folder.getDir());
        File pluginFile = new File(pluginDir, name.endsWith(".jar") ? name : name + ".jar");
        if (!pluginFile.isFile()) {
            for (File f : Objects.requireNonNull(pluginDir.listFiles())) {
                if (f.getName().endsWith(".jar")) {
                    PluginDescriptionFile desc = PluginManager.getPlugin().getPluginLoader().getPluginDescription(f);
                    if (desc.getName().equalsIgnoreCase(name)) {
                        pluginFile = f;
                        break;
                    }
                }
            }
        }
        target = Bukkit.getPluginManager().loadPlugin(pluginFile);
        if (target != null) {
            target.onLoad();
            Bukkit.getPluginManager().enablePlugin(target);
        }
    }

    public static Plugin[] getAll() {
        return Bukkit.getPluginManager().getPlugins();
    }

    public static void reload(Plugin plugin, Folder folder) throws Throwable {
        if (plugin != null) {
            unload(plugin);
            disable(plugin);
            load(plugin, folder);
            for (Plugin all : getAll()) {
                if (!all.equals(plugin)) {
                    if (all.getDescription().getDepend().contains(plugin.getName())) {
                        unload(all);
                        disable(all);
                        load(all, folder);
                    }
                }
            }
        }
    }

    public static void reloadAll(Folder folder) throws Throwable {
        if (folder.equals(Folder.BUKKIT)) {
            for (Plugin plugin : getAll()) {
                reload(plugin, folder);
            }
        } else {
            for (File file : folder.getFile().listFiles()) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    for (Plugin plugin : getAll()) {
                        if (file.getPath().endsWith(plugin.getDataFolder().getPath() + ".jar")) {
                            disable(plugin);
                            unload(plugin);
                            break;
                        }
                    }
                    if (!file.getName().startsWith("!")) {
                        PluginUtil.load(file.getName(), PluginUtil.Folder.MANAGER);
                    }
                }
            }
        }
    }

    public enum Folder {
        BUKKIT(PluginManager.getPlugin().getDataFolder().getAbsoluteFile().getParentFile().getAbsolutePath()),
        MANAGER(PluginManager.getManagerFolder()),
        ;

        private final String dir;
        private final File file;

        Folder(String dir) {
            this.dir = dir;
            this.file = new File(dir).getAbsoluteFile();
        }

        public File getFile() {
            return file;
        }

        public String getDir() {
            return dir;
        }

        @Override
        public String toString() {
            return "Folder{" +
                    "dir='" + dir + '\'' +
                    ", file=" + file +
                    '}';
        }
    }

    public static void unload(Plugin plugin) {
        final String name = plugin.getName();
        final org.bukkit.plugin.PluginManager pluginManager = Bukkit.getPluginManager();
        SimpleCommandMap commandMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        pluginManager.disablePlugin(plugin);
        try {
            Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
            pluginsField.setAccessible(true);
            plugins = (List<Plugin>) pluginsField.get(pluginManager);
        } catch (Exception ignored) {
        }
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
        pluginManager.disablePlugin(plugin);
        if (plugins != null) {
            plugins.remove(plugin);
        }
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
                    if (command.getPlugin() != plugin) {
                        continue;
                    }
                    command.unregister(commandMap);
                    it2.remove();
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
        System.gc();
        plugin.getName();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
