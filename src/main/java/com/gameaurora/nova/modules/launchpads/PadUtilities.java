package com.gameaurora.nova.modules.launchpads;

import org.bukkit.Location;

import com.gameaurora.nova.utilities.DataConfiguration;
import com.gameaurora.nova.utilities.SerializableLocation;

public class PadUtilities {

    public static double getLaunchDistance(Location location) {
        if (DataConfiguration.getConfig().getConfigurationSection("launch-pads") != null) {
            String stringLoc = SerializableLocation.locationToString(location);
            if (DataConfiguration.getConfig().getString("launch-pads." + stringLoc) != null) {
                return DataConfiguration.getConfig().getDouble("launch-pads." + stringLoc);
            }
        }
        return 0;
    }

    public static void setLaunchDistance(Location location, double distance) {
        removeDistancesForLocation(location);
        DataConfiguration.getConfig().set("launch-pads." + SerializableLocation.locationToString(location), distance);
        DataConfiguration.save();
    }

    private static void removeDistancesForLocation(Location location) {
        DataConfiguration.getConfig().set("launch-pads." + SerializableLocation.locationToString(location), null);
        DataConfiguration.save();
    }

}
