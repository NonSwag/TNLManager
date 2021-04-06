package net.nonswag.tnl.manager.api.gui;

import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.manager.api.plugin.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PluginsGUI {

    @Nonnull
    protected static final PluginsGUI instance = new PluginsGUI();

    @Nonnull
    private final Inventory inventory = Bukkit.createInventory(null, 54, "§8» §6§lPlugin§e§lList");
    @Nonnull
    private final ItemStack placeholder1 = TNLItem.create(Material.GRAY_STAINED_GLASS_PANE).setName("§7-§8/§7-").build();
    @Nonnull
    private final ItemStack placeholder2 = TNLItem.create(Material.WHITE_STAINED_GLASS_PANE).setName("§7-§8/§7-").build();

    protected PluginsGUI() {
    }

    @Nonnull
    public Inventory getInventory() {
        inventory.setItem(0, getPlaceholder2());
        inventory.setItem(8, getPlaceholder2());
        inventory.setItem(9, getPlaceholder1());
        inventory.setItem(17, getPlaceholder1());
        inventory.setItem(18, getPlaceholder1());
        inventory.setItem(26, getPlaceholder1());
        inventory.setItem(27, getPlaceholder1());
        inventory.setItem(35, getPlaceholder1());
        inventory.setItem(36, getPlaceholder1());
        inventory.setItem(44, getPlaceholder1());
        inventory.setItem(inventory.getSize() - 9, getPlaceholder2());
        inventory.setItem(inventory.getSize() - 1, getPlaceholder2());
        for (int i = 1; i <= 7; i++) {
            inventory.setItem(i, getPlaceholder1());
        }
        for (int i = inventory.getSize() - 8; i <= inventory.getSize() - 2; i++) {
            inventory.setItem(i, getPlaceholder1());
        }
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            List<String> authors = plugin.getDescription().getAuthors();
            if (authors.size() == 1) {
                inventory.addItem(TNLItem.create(Material.BOOK).setName("§8* §r" + PluginManager.getInstance().getName(plugin, true)).setLore("§8* §7Author§8: §6" + authors.get(0)).build());
            } else {
                List<String> lore = new ArrayList<>();
                lore.add("§8* §7Authors§8: §6" + authors.size());
                for (String s : authors) {
                    lore.add("§6" + s);
                }
                inventory.addItem(TNLItem.create(Material.BOOK).setName("§8* §r" + PluginManager.getInstance().getName(plugin, true)).setLore(lore).build());
            }
        }
        return inventory;
    }

    @Nonnull
    public ItemStack getPlaceholder1() {
        return placeholder1;
    }

    @Nonnull
    public ItemStack getPlaceholder2() {
        return placeholder2;
    }

    @Nonnull
    public static PluginsGUI getInstance() {
        return instance;
    }
}
