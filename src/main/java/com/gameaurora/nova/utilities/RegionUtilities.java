package com.gameaurora.nova.utilities;

import static com.sk89q.worldguard.bukkit.BukkitUtil.toVector;

import java.util.Iterator;

import org.bukkit.Location;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionUtilities {
    
    public static String getRegion(Location location) {
        String region = null;
        Iterator<ProtectedRegion> regions = WGBukkit.getRegionManager(location.getWorld()).getApplicableRegions(toVector(location)).iterator();
        while (regions.hasNext()) {
            String next = regions.next().getId();
            if (!next.equalsIgnoreCase("__global__")) {
                return next;
            }
        }
        return region;
    }

}
