package com.gameaurora.nova.general;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum AuroraServer {

    SANDBOX("Sandbox", "Our private box of undedicated sand.", new ItemStack(Material.SAND)),
    LIMBO("Limbo", "An intermediate state or condition.", new ItemStack(Material.BEDROCK)),
    HUB("Hub", "Our amazing server hub.", new ItemStack(Material.SLIME_BALL)),
    SURVIVAL("Survival", "Anti-griefing Minecraft PvE survival!", new ItemStack(Material.LAVA_BUCKET)),
    SKYBLOCK("Skyblock", "Island survival with limited resources!", new ItemStack(Material.GRASS)),
    SPLEEF("Spleef", "Classic spleef, knock out other players!", new ItemStack(Material.SNOW_BALL)),
    BOWSPLEEF("Bow Spleef", "Spleef with a twist, use a bow!", new ItemStack(Material.BOW)),
    TF2("TF2", "Team Fortress 2 in Minecraft!", new ItemStack(Material.REDSTONE)),
    TNTRUN("TNT Run", "Run around and don't fall!", new ItemStack(Material.TNT)),
    DEADLYDROP("Deadly Drop", "Get to the bottom first without dying!", new ItemStack(Material.GOLD_BOOTS)),
    CREATIVE("Creative", "Access is limited to VIP and above." + "\n" + ChatColor.GRAY + "Buy a rank at: " + ChatColor.AQUA + "www.gameaurora.com/store", new ItemStack(Material.MILK_BUCKET)),
    BUILDTEAM("Build Team", "An amazing place for amazing people and cute kittens.", new ItemStack(Material.BEACON)),
    FACTIONS("Factions", "Classic factions with griefing, raiding, and more!", new ItemStack(Material.OBSIDIAN));

    private final String name;
    private final String description;
    private final ItemStack icon;
    private int playerCount = -1;

    private AuroraServer(String name, String description, ItemStack icon) {
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    public String getName() {
        return this.toString().toLowerCase();
    }

    public String getPrettyName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int i) {
        playerCount = i;
    }

}
