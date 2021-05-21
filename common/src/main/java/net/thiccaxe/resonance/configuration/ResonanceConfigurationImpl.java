/*
 * This file is part of Resonance, licensed under the MIT License.
 *
 * Copyright (c) 2021 thiccaxe
 * Copyright (c) 2021 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ResonanceConfigurationImpl<C extends ConfigValues> implements Feature, ResonanceConfiguration<C> {
    private boolean enabled = false;

    protected File configFile;
    protected final @NotNull File dataFolder;
    protected final @NotNull ResonancePlugin plugin;
    protected final @NotNull ResonanceLogger featureLogger;

    private ConfigurationLoader<CommentedConfigurationNode> defaultLoader;
    protected ConfigurationLoader<CommentedConfigurationNode> loader;
    protected ConfigurationNode root;


    protected ResonanceConfigurationImpl(@NotNull ResonancePlugin plugin, @NotNull File dataFolder) {
        this.plugin = plugin;
        this.featureLogger = plugin.logger();
        this.dataFolder = dataFolder;
    }


    @Override
    public void enable() {
        if (loader == null) {
            setupFile();
        } else {
            reloadConfig();
        }
        enabled = true;
    }

    @Override
    public void disable() {
        try {
            loader.save(root);
            plugin.logger().info("Saved Config.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        enabled = false;
    }

    @Override
    public void reload() {
        reloadConfig();
    }

    @Override
    public ConfigurationNode root() {
        return root;
    }

    @Override
    public @NotNull ResonanceLogger logger() {
        return featureLogger;
    }

    @Override
    public void save() {
        if (!enabled) return;
        try {
            logger().info("Saving Config... ");
            loader.save(root);
        } catch (ConfigurateException e) {
            logger().warn("Failed to Reload Config! ");
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

    protected void reloadConfig() {
        try {
            logger().info("Reloading Config... ");
            initConfig();
            try {
                if (root != null) {
                    getConfigValues(root);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            logger().warn("Failed to Reload Config! ");
            e.printStackTrace();
        }
    }

    protected void initConfig() {
        logger().info("Initializing Config... ");
        try {
            File defaultConfig = new File(dataFolder, getDefaultConfigName());
            if (!defaultConfig.exists()) {
                createFile(defaultConfig);
                FileOutputStream outputStream = new FileOutputStream(defaultConfig, false);
                InputStream inputStream = getDefaultConfigInputStream();
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                outputStream.write(buffer);
                outputStream.close();
            }
            if (defaultLoader == null) {
                defaultLoader = HoconConfigurationLoader.builder()
                        .file(defaultConfig)
                        .emitComments(false)
                        .build();
            }
            ConfigurationNode defaultConfigNode = defaultLoader.load();
            root = loader.load();
            root.mergeFrom(defaultConfigNode);
        } catch (ConfigurateException e) {
            logger().warn("Failed to merge config: ");
            e.printStackTrace();
        } catch (IOException e) {
            logger().warn("Failed to create default config: ");
            e.printStackTrace();
        }
    }

    protected abstract void getConfigValues(@NotNull ConfigurationNode root);



    @Override
    public boolean enabled() {
        return enabled;
    }


    protected abstract String getFileName();

    protected abstract InputStream getDefaultConfigInputStream();


    protected String getDefaultConfigName() {
        return "DEFAULT_CONFIG.conf";
    }

}
