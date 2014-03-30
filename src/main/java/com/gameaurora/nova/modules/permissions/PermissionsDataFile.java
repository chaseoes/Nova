package com.gameaurora.nova.modules.permissions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gameaurora.nova.Nova;

public class PermissionsDataFile {
	
    private static FileConfiguration customConfig = null;
    private static File customConfigFile = null;
    
    public static void reload() {
        if (customConfigFile == null) {
            customConfigFile = new File(Nova.getInstance().getDataFolder(), "permissions.yml");
        }

        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        InputStream defConfigStream = Nova.getInstance().getResource("permissions.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }

    public static FileConfiguration getDataFile() {
        if (customConfig == null) {
            reload();
        }
        return customConfig;
    }

    public static void save() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getDataFile().save(customConfigFile);
        } catch (IOException ex) {

        }
    }
    
}
