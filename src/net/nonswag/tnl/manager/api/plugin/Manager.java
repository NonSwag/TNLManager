package net.nonswag.tnl.manager.api.plugin;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface Manager {

    void loadAll() throws InvalidDescriptionException, InvalidPluginException;

    void reloadAll() throws InvalidDescriptionException, InvalidPluginException;

    void enableAll();

    void disableAll();

    void unloadAll();

    void load(@Nonnull String name) throws InvalidDescriptionException, InvalidPluginException;

    void reload(@Nonnull Plugin plugin) throws InvalidDescriptionException, InvalidPluginException;

    void enable(@Nonnull Plugin plugin);

    void disable(@Nonnull Plugin plugin);

    void unload(@Nonnull Plugin plugin);

    @Nullable
    Plugin getPlugin(@Nonnull String name);

    @Nonnull
    Plugin[] getPlugins();

    @Nonnull
    List<String> getPlugins(boolean includeVersion);

    @Nonnull
    String getName(@Nonnull Plugin plugin, boolean includeVersion);

    @Nonnull
    String getVersion(@Nonnull Plugin plugin);
}
