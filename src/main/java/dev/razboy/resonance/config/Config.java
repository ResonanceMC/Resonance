package dev.razboy.resonance.config;

import dev.razboy.resonance.Resonance;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.IOException;

public abstract class Config extends YamlConfiguration {

    protected File file;
    protected abstract String getFileName();
    protected abstract void initializeConfig();
    public void setupFile(File dataFolder) {
        file = new File(dataFolder, getFileName());
        createIfNotExists();
        // load it if it already exists
        reload();
        // default any missing values
        initializeConfig();
        // persist any changes
        save();
    }
    public void save() {
        try {
            this.save(file);
        } catch (IOException | YAMLException e) {
            Resonance.log("Failed to save file: " + getFileName());
            e.printStackTrace();
            reload();
        }
    }

    /**
     * Reload the configuration file.
     */
    public void reload() {
        try {
            this.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Resonance.log("Failed to load file: " + getFileName());
            e.printStackTrace();
        }
    }

    /**
     * Create the physical file if it doesn't exist.
     */
    private void createIfNotExists() {
        if (file.exists()) {
            return;
        }

        try {
            file.createNewFile();
            Resonance.log("Created " + getFileName());
        } catch (Exception e) {
            Resonance.log("Failed to create file: " + getFileName());
            e.printStackTrace();
        }
    }
}
