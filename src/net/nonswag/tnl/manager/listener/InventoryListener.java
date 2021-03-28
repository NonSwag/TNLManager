package net.nonswag.tnl.manager.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§8» §6§lPlugin§e§lList")) {
            event.setCancelled(true);
        }
    }
}
