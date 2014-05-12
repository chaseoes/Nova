package com.gameaurora.nova.utilities;

import com.gameaurora.nova.Nova;

import java.sql.*;
import java.util.logging.Level;

public class SQLUtilities {

    private final Nova plugin;
    private Connection conn;
    private boolean connected = false;


    public SQLUtilities(final Nova p) {
        plugin = p;
        String username = p.getConfig().getString("database.username");
        String password = p.getConfig().getString("database.password");
        String url = "jdbc:mysql://" + p.getConfig().getString("database.hostname") + ":" + p.getConfig().getInt("database.port") + "/" + p.getConfig().getString("database.database_name");

        try {
            conn = DriverManager.getConnection(url, username, password);
            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
            connected = false;
            plugin.getLogger().log(Level.SEVERE, "Could not connect to database! Verify your database details in the configuration are correct.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public Connection getConnection() {
        if (connected) {
            return conn;
        }
        return null;
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
            connected = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

