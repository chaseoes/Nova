package com.gameaurora.nova.modules.lobbybuffs;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gameaurora.nova.Nova;

public enum BuffType {

    SUPER_SPEED;

    public boolean isEnabled() {
        return Nova.getInstance().getModule("lobby-buffs").getConfig().getStringList("buffs").contains(this.toString().toUpperCase());
    }

    public void apply(final Player player) {
        final BuffType instance = this;
        Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
            public void run() {
                switch (instance) {
                case SUPER_SPEED:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
                    return;
                }
            }
        }, 1L);
    }

}
