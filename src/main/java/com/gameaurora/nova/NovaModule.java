package com.gameaurora.nova;

import org.bukkit.configuration.ConfigurationSection;

import com.gameaurora.nova.utilities.ModuleConfiguration;

public class NovaModule {

    private String moduleName;
    private ConfigurationSection config;

    public NovaModule(String moduleName) {
        this.moduleName = moduleName;
        this.config = ModuleConfiguration.getConfig().getConfigurationSection(moduleName);
    }

    public String getName() {
        return moduleName;
    }

    public boolean isEnabled() {
        return config != null && config.getBoolean("enabled");
    }

    public ConfigurationSection getConfig() {
        return config;
    }

}
