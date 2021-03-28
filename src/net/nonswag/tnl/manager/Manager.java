package net.nonswag.tnl.manager;

import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.event.EventManager;
import net.nonswag.tnl.listener.api.file.JsonConfig;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.manager.api.plugin.PluginManager;
import net.nonswag.tnl.manager.commands.PluginCommand;
import net.nonswag.tnl.manager.listener.CommandListener;
import net.nonswag.tnl.manager.completer.PluginCommandTabComplete;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class Manager extends JavaPlugin {

    protected static Manager instance = null;

    @Nonnull
    private final JsonConfig configuration = new JsonConfig("plugins/TNLManager/", "config.json");

    private boolean pluginsGUI = true;
    private boolean everyoneCanSeePlugins = true;

    @Override
    public void onEnable() {
        setInstance(this);
        if (PluginManager.getInstance().getPlugin("TNLListener") != null) {
            if (Settings.AUTO_UPDATER.getValue()) {
                new PluginUpdate(this).downloadUpdate();
            }
        }
        CommandManager commandManager = new CommandManager(this);
        EventManager eventManager = new EventManager(this);
        commandManager.registerCommand("plugin", "tnl.manage", new PluginCommand(), new PluginCommandTabComplete());
        eventManager.registerListener(new CommandListener());
        if (!getConfiguration().getJsonElement().getAsJsonObject().has("plugins-gui")) {
            getConfiguration().getJsonElement().getAsJsonObject().addProperty("plugins-gui", isPluginsGUI());
        }
        if (!getConfiguration().getJsonElement().getAsJsonObject().has("everyone-can-see-plugins")) {
            getConfiguration().getJsonElement().getAsJsonObject().addProperty("everyone-can-see-plugins", isEveryoneCanSeePlugins());
        }
        setPluginsGUI(getConfiguration().getJsonElement().getAsJsonObject().get("plugins-gui").getAsBoolean());
        setEveryoneCanSeePlugins(getConfiguration().getJsonElement().getAsJsonObject().get("everyone-can-see-plugins").getAsBoolean());
        getConfiguration().save();
    }

    private static void setInstance(@Nonnull Manager instance) {
        Manager.instance = instance;
    }

    public static Manager getInstance() {
        return instance;
    }

    @Nonnull
    public JsonConfig getConfiguration() {
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
