package com.gameaurora.nova.modules.adminmode;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.PlayerStateStorage;

public class AdminModeCommand implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cs instanceof Player) {
            Player player = (Player) cs;
            if (player.hasPermission("nova.adminmode")) {
                if (Nova.getInstance().adminPlayers.contains(player.getName())) {
                	Nova.getInstance().adminPlayerStates.get(player.getName()).clear();
                	Nova.getInstance().adminPlayerStates.get(player.getName()).restore();
                	Nova.getInstance().adminPlayerStates.remove(player.getName());
                	Nova.getInstance().adminPlayers.remove(player.getName());
                    cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Admin mode disabled.");
                } else {
                	Nova.getInstance().adminPlayerStates.put(player.getName(), new PlayerStateStorage(player));
                	Nova.getInstance().adminPlayerStates.get(player.getName()).clear();
                	Nova.getInstance().adminPlayers.add(player.getName());
                    giveAdminKit(player);
                    player.setGameMode(GameMode.CREATIVE);
                    cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Admin mode enabled.");
                }
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                player.updateInventory();
            } else {
                cs.sendMessage(NovaMessages.NO_PERMISSION);
            }
        } else {
            cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
        }
        return true;
    }
    
    private void giveAdminKit(Player player) {
    	player.getInventory().setItem(2, getAdminItem(Material.WOOD_AXE, "Admin Wand"));
    	player.getInventory().setItem(3, getAdminItem(Material.LEATHER, "Admin Region Tool"));
    	player.getInventory().setItem(4, getAdminItem(Material.INK_SACK, "Admin Resource Pack Changer"));
        player.getInventory().setItem(5, getAdminItem(Material.BEDROCK, "Admin Bedrock"));
        player.getInventory().setItem(6, getAdminItem(Material.COMPASS, "Admin Compass"));
        player.getInventory().setItem(7, getAdminItem(Material.WATCH, "Admin Clock"));
        player.getInventory().setItem(8, getAdminItem(Material.WOOD_HOE, "Admin Welcomer"));
        
        player.getInventory().setHelmet(getAdminItem(Material.LEATHER_HELMET, "Admin Helmet"));
        player.getInventory().setChestplate(getAdminItem(Material.LEATHER_CHESTPLATE, "Admin Chestplate"));
        player.getInventory().setLeggings(getAdminItem(Material.LEATHER_LEGGINGS, "Admin Leggings"));
        player.getInventory().setBoots(getAdminItem(Material.LEATHER_BOOTS, "Admin Boots"));
    }
    
    public ItemStack getAdminItem(Material m, String name) {
        ItemStack i = new ItemStack(m, 1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.RED + name);
        
        if (m == Material.LEATHER_HELMET || m == Material.LEATHER_CHESTPLATE || m == Material.LEATHER_LEGGINGS || m == Material.LEATHER_BOOTS) {
        	LeatherArmorMeta lm = (LeatherArmorMeta) meta;
        	lm.setColor(Color.RED);
        	i.setItemMeta(lm);
        }
        
        i.setItemMeta(meta);
        return i;
    }

}
