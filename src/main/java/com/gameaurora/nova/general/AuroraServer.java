package com.gameaurora.nova.general;

public enum AuroraServer {

    SANDBOX("Sandbox"), LIMBO("Limbo"), HUB("Hub"), SURVIVAL("Survival"), SG("Survival Games"), SKYBLOCK("Skyblock"), SPLEEF("Spleef"), BOWSPLEEF("Bow Spleef"), TF2("TF2"), TNTRUN("TNT Run"), DEADLYDROP("Deadly Drop"), CREATIVE("Creative"), BUILDTEAM("Build Team"), FACTIONS("Factions");

    private final String name;

    private AuroraServer(String name) {
        this.name = name;
    }

    public String getName() {
        return this.toString().toLowerCase();
    }

    public String getPrettyName() {
        return name;
    }

}
