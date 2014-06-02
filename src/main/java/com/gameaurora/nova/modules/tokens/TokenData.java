package com.gameaurora.nova.modules.tokens;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;

public class TokenData {

    private static ConfigurationSection tokenData = TokenDataFile.getDataFile().getConfigurationSection("players");

    public static void load() {
        if (tokenData == null) {
            TokenDataFile.getDataFile().createSection("players");
            tokenData = TokenDataFile.getDataFile().getConfigurationSection("players");
        }
    }

    /**
     * Adds the specified amount of tokens to a player's profile.
     * 
     * @param player the player object to add tokens to
     * @param amount the amount of tokens to add
     * @return the amount of tokens the player has after adding
     */
    public static int addTokens(Player player, int amount) {
        int i = addTokens(player.getUniqueId(), amount);
        Nova.getInstance().updateScoreboard(player);
        return i;
    }

    /**
     * Adds the specified amount of tokens to a UUID.
     * 
     * @param uuid the UUID object to add tokens to
     * @param amount the amount of tokens to add
     * @return the amount of tokens the uuid has after adding
     */
    public static int addTokens(UUID uuid, int amount) {
        return setTokens(uuid, getTokens(uuid) + amount);
    }

    /**
     * Sets the amount of tokens a player has.
     * 
     * @param player the player object in which tokens should be set for
     * @param tokens the amount of tokens to set
     * @return the amount of tokens the player has after setting
     */
    public static int setTokens(Player player, int tokens) {
        int i = setTokens(player.getUniqueId(), tokens);
        Nova.getInstance().updateScoreboard(player);
        return i;
    }

    /**
     * Sets the amount of tokens a UUID has.
     * 
     * @param uuid the UUID object in which tokens should be set for
     * @param tokens the amount of tokens to set
     * @return the amount of tokens the UUID has after setting
     */
    public static int setTokens(UUID uuid, int tokens) {
        tokenData.set(uuid.toString() + ".tokens", tokens);
        TokenDataFile.saveDataFile();
        return getTokens(uuid);
    }

    /**
     * Returns the amount of tokens a player has.
     * 
     * @param player the player object to return tokens for
     * @return the amount of tokens the player has
     */
    public static int getTokens(Player player) {
        return getTokens(player.getUniqueId());
    }

    /**
     * Returns the amount of tokens a UUID has.
     * 
     * @param uuid the UUID object to return tokens for
     * @return the amount of tokens the UUID has
     */
    public static int getTokens(UUID uuid) {
        if (tokenData.getString(uuid.toString() + ".tokens") != null) {
            return tokenData.getInt(uuid.toString() + ".tokens");
        }
        return 0;
    }

}
