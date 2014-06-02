package com.gameaurora.nova.modules.tokens;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gameaurora.nova.Nova;
import com.google.common.io.Files;

public class TokenDataFile {

    private static File customConfigFile = null;
    private static YamlConfiguration customConfig = null;

    public static boolean reloadDataFile() {
        try {
            if (customConfigFile == null) {
                customConfigFile = new File(Nova.getInstance().getDataFolder(), "data.yml");
                customConfigFile.createNewFile();
            }

            customConfig = new YamlConfiguration();
            customConfig.loadFromString(Files.toString(customConfigFile, Charset.forName("UTF-8")));

            InputStream defConfigStream = Nova.getInstance().getResource("data.yml");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                customConfig.setDefaults(defConfig);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static FileConfiguration getDataFile() {
        if (customConfig == null) {
            reloadDataFile();
        }
        return customConfig;
    }

    public static void saveDataFile() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            String data = customConfig.saveToString();
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(customConfigFile), "UTF-8");
            out.write(data, 0, data.length());
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
