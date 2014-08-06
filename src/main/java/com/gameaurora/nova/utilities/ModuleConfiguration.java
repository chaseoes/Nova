package com.gameaurora.nova.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gameaurora.nova.Nova;

public class ModuleConfiguration {
    private static File customConfigFile = null;
    private static final String fileName = "modules.yml";
    private static FileConfiguration customConfig = null;

    public static FileConfiguration getConfig() {
        if (customConfig == null) {
            reload();
        }
        return customConfig;
    }

    public static void reload() {
        if (customConfigFile == null) {
            customConfigFile = new File(Nova.getInstance().getDataFolder(), fileName);
        }

        Reader defConfigStream;
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        try {
            defConfigStream = new InputStreamReader(Nova.getInstance().getResource(fileName), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                customConfig.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }

        try {
            getConfig().save(customConfigFile);
        } catch (IOException ex) {

        }
    }

}
