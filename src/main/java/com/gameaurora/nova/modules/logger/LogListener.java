package com.gameaurora.nova.modules.logger;

import org.bukkit.Location;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import com.gameaurora.nova.Nova;

public class LogListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if (Nova.getInstance().getModule("logger").getConfig().getStringList("log").contains("HOPPER")) {
            if (event.getInitiator().getHolder() instanceof Hopper) {
                Hopper hopper = (Hopper) event.getInitiator().getHolder();
                Location hopperLocation = hopper.getLocation();
                LoggerUtilities.log("X: " + hopperLocation.getBlockX() + " Y: " + hopperLocation.getBlockY() + " Z: " + hopperLocation.getBlockZ() + " CANCELLED: " + event.isCancelled(), LogEntryType.HOPPER);
            }
        }
    }

}
