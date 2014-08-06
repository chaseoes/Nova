package com.gameaurora.nova.modules.giveitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.GeneralUtilities;

public enum ItemType {

    SERVER_SELECTOR, TRAIL_SELECTOR, TELEPORT_BOW;

    public boolean isEnabled() {
        return Nova.getInstance().getModule("give-items").getConfig().getStringList("items").contains(this.toString().toUpperCase());
    }

    public void add(Player player) {
        switch (this) {
        case SERVER_SELECTOR:
            player.getInventory().addItem(GeneralUtilities.getCustomItem(Material.COMPASS, ChatColor.AQUA + "Server Menu", ChatColor.GRAY + "Right click to open."));
            return;
        case TRAIL_SELECTOR:
            player.getInventory().addItem(GeneralUtilities.getCustomItem(Material.REDSTONE, ChatColor.GREEN + "Trail Selector", ChatColor.GRAY + "Right click to open."));
            return;
        case TELEPORT_BOW:
            ItemStack bow = GeneralUtilities.getCustomItem(Material.BOW, ChatColor.GREEN + "Teleport Bow", ChatColor.GRAY + "Shoot to teleport!");
            bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            player.getInventory().addItem(bow);
            player.getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
            return;
        }
    }

}
