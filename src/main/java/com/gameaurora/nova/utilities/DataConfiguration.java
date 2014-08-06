package com.gameaurora.nova.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraft.util.org.apache.commons.io.IOUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gameaurora.nova.Nova;

public class DataConfiguration {
    private static File customConfigFile = null;
    private static final String fileName = "data.yml";
    private static FileConfiguration customConfig = null;

    public static FileConfiguration getConfig() {
        if (customConfig == null) {
            reload();
        }
        return customConfig;
    }

    public static void reload() {
        try {
            if (customConfigFile == null) {
                customConfigFile = new File(Nova.getInstance().getDataFolder(), fileName);
            }

            customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
            InputStream inputStream = Nova.getInstance().getResource(fileName);

            if (inputStream != null) {
                File file = null;
                OutputStream outputStream = new FileOutputStream(file);
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
                customConfig.setDefaults(defConfig);
            }
        } catch (Exception e) {

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
