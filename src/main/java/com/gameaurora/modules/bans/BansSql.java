package com.gameaurora.modules.bans;

import com.gameaurora.nova.Nova;

import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class BansSql {

    static void createTables() {
        final Connection conn = Nova.getInstance().getSQL().getConnection();
        if (conn == null) {
            System.err.println("Can't sync bans, no mysql connection!");
            return;
        }
        Statement tableCreation = null;
        try {
            tableCreation = conn.createStatement();
            tableCreation.executeUpdate("CREATE TABLE IF NOT EXISTS BANS(MSB BIGINT NOT NULL, LSB BIGINT NOT NULL, Reason TEXT NOT NULL, PRIMARY KEY (MSB, LSB));");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (tableCreation != null) {
                try {
                    tableCreation.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static boolean banPlayer(UUID uuid, String reason) {
        Connection conn = Nova.getInstance().getSQL().getConnection();
        if (conn == null) {
            System.err.println("Can't ban player, no mysql connection!");
            return false;
        }
        PreparedStatement banStatement = null;
        try {
            banStatement = conn.prepareStatement("INSERT INTO BANS VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE Reason=?;");
            banStatement.setLong(1, uuid.getMostSignificantBits());
            banStatement.setLong(2, uuid.getLeastSignificantBits());
            banStatement.setString(3, reason);
            banStatement.setString(4, reason);
            banStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (banStatement != null) {
                try {
                    banStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    static boolean unbanPlayer(UUID uuid) {
        Connection conn = Nova.getInstance().getSQL().getConnection();
        if (conn == null) {
            System.err.println("Can't unban player, no mysql connection!");
            return false;
        }
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("DELETE FROM BANS WHERE MSB=? AND LSB=?");
            statement.setLong(1, uuid.getMostSignificantBits());
            statement.setLong(2, uuid.getLeastSignificantBits());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    static void syncBans(Map<UUID, String> bans) {
        Connection conn = Nova.getInstance().getSQL().getConnection();
        if (conn == null) {
            System.err.println("Can't sync bans, no mysql connection!");
            return;
        }
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("SELECT msb, lsb, Reason FROM BANS;");
            ResultSet set = statement.executeQuery();
            synchronized (bans) {
                bans.clear();
                while (set.next()) {
                    UUID uuid = new UUID(set.getLong(1), set.getLong(2));
                    bans.put(uuid, set.getString(3));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
