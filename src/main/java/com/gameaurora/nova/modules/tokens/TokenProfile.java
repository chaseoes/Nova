package com.gameaurora.nova.modules.tokens;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

public class TokenProfile {

    private UUID uuid;

    public TokenProfile(OfflinePlayer player) {
        uuid = player.getUniqueId();
    }

    public int getTokens() {
        return TokenData.getTokens(uuid);
    }

    public int addTokens(int tokens) {
        return TokenData.addTokens(uuid, tokens);
    }

    public int setTokens(int tokens) {
        return TokenData.setTokens(uuid, tokens);
    }

}
