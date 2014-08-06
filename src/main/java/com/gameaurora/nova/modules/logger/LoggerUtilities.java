package com.gameaurora.nova.modules.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;

import com.gameaurora.nova.Nova;

public class LoggerUtilities {

    public static void log(String string, LogEntryType type) {
        try {
            File log = new File(Nova.getInstance().getDataFolder() + "/logs/" + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".txt");
            log.getParentFile().mkdirs();
            log.createNewFile();

            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(log, true)));
            out.println(format(string, type));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String format(String string, LogEntryType type) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return "[" + type.toString().toUpperCase().trim() + "] [" + dateFormat.format(date) + "] " + ChatColor.stripColor(string);
    }

}
