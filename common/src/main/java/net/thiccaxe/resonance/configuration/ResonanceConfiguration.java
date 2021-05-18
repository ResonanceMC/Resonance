package net.thiccaxe.resonance.configuration;

import net.thiccaxe.resonance.feature.Feature;
import net.thiccaxe.resonance.logging.ResonanceLogger;
import net.thiccaxe.resonance.plugin.ResonancePlugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.*;
import java.util.EnumMap;

public abstract class ResonanceConfiguration<E extends Enum<E>> implements Feature {

    protected File configFile;
    protected final @NotNull File dataFolder;
    protected final @NotNull ResonancePlugin plugin;
    protected final @NotNull ResonanceLogger logger;

    private ConfigurationLoader<CommentedConfigurationNode> defaultLoader;
    protected ConfigurationLoader<CommentedConfigurationNode> loader;
    protected ConfigurationNode root;


    protected ResonanceConfiguration(@NotNull ResonancePlugin plugin, @NotNull File dataFolder) {
        this.plugin = plugin;
        this.logger = plugin.logger();
        this.dataFolder = dataFolder;
    }


    @Override
    public void enable() {
        createDirectory(dataFolder);
    }

    protected void createDirectory(File folder) {
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } catch (Exception e) {
            plugin.logger().warn("Failed to create: " + folder.getAbsolutePath());
            e.printStackTrace();
        }
    }

    protected void createFile(File file) {
        if (file.exists()) return;

        try {
            file.getParentFile().mkdir();
            file.createNewFile();
        } catch (IOException e) {
            plugin.logger().warn("Failed to create file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    protected void setupFile() {
        configFile = new File(dataFolder, getFileName());
        createFile(configFile);

        loader = HoconConfigurationLoader.builder()
                .file(configFile)
                .emitComments(false)
                .build();
        reloadConfig();
    }

    protected void reloadConfig() {
        try {
            plugin.logger().info("Reloading Config... ");
            initConfig();
            if (defaultLoader.load())
        }
    }

    protected void initConfig() {
        plugin.logger().info("Initializing Config... ");
        try {
            File defaultConfig = new File(dataFolder, getDefaultConfigName());
            createFile(defaultConfig);
            FileOutputStream outputStream = new FileOutputStream(defaultConfig, false);
            InputStream inputStream = getDefaultConfigInputStream();
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            outputStream.write(buffer);
            outputStream.close();
            if (defaultLoader == null) {
                defaultLoader = HoconConfigurationLoader.builder()
                        .file(defaultConfig)
                        .emitComments(false)
                        .build();
            }
            ConfigurationNode defaultConfigNode = defaultLoader.load();
            ConfigurationNode rootNode = loader.load();
            rootNode.mergeFrom(defaultConfigNode);
        } catch (ConfigurateException e) {
            plugin.logger().warn("Failed to merge config: ");
            e.printStackTrace();
        } catch (IOException e) {
            plugin.logger().warn("Failed to create default config: ");
            e.printStackTrace();
        }
    }

    @Override
    public void disable() {

    }

    @Override
    public boolean enabled() {
        return false;
    }


    protected abstract String getFileName();

    protected abstract InputStream getDefaultConfigInputStream();

    public abstract EnumMap<E, ConfigurationNode> getConfig();

    protected String getDefaultConfigName() {
        return "DEFAULT_CONFIG.conf";
    }

}
