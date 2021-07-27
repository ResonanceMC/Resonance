package dev.razboy.resonance.config.impl;

import dev.razboy.resonance.config.Config;

public class DefaultConfig extends Config {
    @Override
    protected String getFileName() {
        return "config.yml";
    }

    @Override
    protected void initializeConfig() {
        this.options().header("=== Resonance Config === #");
        this.addDefault("Domain", "localhost");
        this.addDefault("Secure", false);
        this.addDefault("LoggedInMessage", "test");
        this.options().copyDefaults(true);
    }
}
