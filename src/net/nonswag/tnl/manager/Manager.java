package net.nonswag.tnl.manager;

import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.event.EventManager;
import net.nonswag.tnl.listener.api.config.PropertyConfig;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.manager.commands.PluginCommand;
import net.nonswag.tnl.manager.listener.CommandListener;
import net.nonswag.tnl.manager.completer.PluginCommandTabComplete;
import net.nonswag.tnl.manager.listener.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class Manager extends JavaPlugin {

    protected static Manager instance = null;

    @Nonnull
    private final PropertyConfig configuration = new PropertyConfig("plugins/Manager/", "config.properties");

    private boolean pluginsGUI = true;
    private boolean everyoneCanSeePlugins = true;

    @Override
    public void onEnable() {
        setInstance(this);
        CommandManager commandManager = new CommandManager(this);
        EventManager eventManager = new EventManager(this);
        commandManager.registerCommand("plugin", "tnl.manage", new PluginCommand(), new PluginCommandTabComplete());
        eventManager.registerListener(new CommandListener());
        eventManager.registerListener(new InventoryListener());
        if (!getConfiguration().has("plugins-gui")) {
            getConfiguration().setValue("plugins-gui", isPluginsGUI());
        }
        if (!getConfiguration().has("everyone-can-see-plugins")) {
            getConfiguration().setValue("everyone-can-see-plugins", isEveryoneCanSeePlugins());
        }
        setPluginsGUI(getConfiguration().getBoolean("plugins-gui"));
        setEveryoneCanSeePlugins(getConfiguration().getBoolean("everyone-can-see-plugins"));
        getConfiguration().save();
        if (Settings.AUTO_UPDATER.getValue()) {
            new PluginUpdate(this).downloadUpdate();
        }
    }

    private static void setInstance(@Nonnull Manager instance) {
        Manager.instance = instance;
    }

    public static Manager getInstance() {
        return instance;
    }

    @Nonnull
    public PropertyConfig getConfiguration() {
        return configuration;
    }

    public boolean isPluginsGUI() {
        return pluginsGUI;
    }

    public void setPluginsGUI(boolean pluginsGUI) {
        this.pluginsGUI = pluginsGUI;
    }

    public boolean isEveryoneCanSeePlugins() {
        return everyoneCanSeePlugins;
    }

    public void setEveryoneCanSeePlugins(boolean everyoneCanSeePlugins) {
        this.everyoneCanSeePlugins = everyoneCanSeePlugins;
    }
}
