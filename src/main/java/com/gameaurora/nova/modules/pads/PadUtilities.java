package com.gameaurora.nova.modules.pads;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.SerializableLocation;

public class PadUtilities {

	public static void setLaunchDistance(Location location, double distance) {
		removeDistancesForLocation(location);
		List<String> launchPadLocations = new ArrayList<String>();
		for (String s : Nova.getInstance().getConfig().getStringList("launch-pads." + String.valueOf(distance).replace(".", "-"))) {
			launchPadLocations.add(s);
		}

		launchPadLocations.add(SerializableLocation.locationToString(location));
		Nova.getInstance().getConfig().set("launch-pads." + String.valueOf(distance).replace(".", "-"), launchPadLocations);
		Nova.getInstance().saveConfig();
	}

	public static double getLaunchDistance(Location location) {
		for (String dist : Nova.getInstance().getConfig().getConfigurationSection("launch-pads").getKeys(false)) {
			double distance = Double.valueOf(dist.replace("-", "."));
			List<String> launchPadLocations = Nova.getInstance().getConfig().getStringList("launch-pads." + String.valueOf(distance).replace(".", "-"));
			if (launchPadLocations.contains(SerializableLocation.locationToString(location))) {
				return distance;
			}
		}
		return 0;
	}

	private static void removeDistancesForLocation(Location location) {
		if (Nova.getInstance().getConfig().getConfigurationSection("launch-pads") == null) {
			Nova.getInstance().getConfig().createSection("launch-pads");
			Nova.getInstance().saveConfig();
		}

		for (String dist : Nova.getInstance().getConfig().getConfigurationSection("launch-pads").getKeys(false)) {
			double distance = Double.valueOf(dist.replace("-", "."));
			List<String> locations = Nova.getInstance().getConfig().getStringList("launch-pads." + String.valueOf(distance).replace(".", "-"));
			System.out.print(dist + " " + distance + " REMOVING: " + SerializableLocation.locationToString(location));
			locations.remove(SerializableLocation.locationToString(location));
			Nova.getInstance().getConfig().set("launch-pads." + String.valueOf(distance).replace(".", "-"), locations);
			Nova.getInstance().saveConfig();
		}


	}

}
