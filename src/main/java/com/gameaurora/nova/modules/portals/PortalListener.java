package com.gameaurora.nova.modules.portals;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gameaurora.nova.events.RegionEnterEvent;
import com.gameaurora.nova.utilities.BungeeUtilities;

public class PortalListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegionEnter(RegionEnterEvent event) {
        if (event.getRegionName().startsWith("server-")) {
            String server = event.getRegionName().split("-")[1];
            BungeeUtilities.sendToServer(event.getPlayer(), server);
        }
    }

}
