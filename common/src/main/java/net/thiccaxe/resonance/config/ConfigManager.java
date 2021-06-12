/*
 * A substantial portion of this file was licensed under the following license:
 *
 * This file is part of MiniMOTD, licensed under the MIT License.
 *
 * Copyright (c) 2021 Jason Penilla
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

package net.thiccaxe.resonance.config;

import net.thiccaxe.resonance.Resonance;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

public final class ConfigManager {

    private final Resonance resonance;

    private final ConfigLoader<ResonanceConfig> mainConfigLoader;
    private ResonanceConfig mainConfig;

    public ConfigManager(final @NotNull Resonance resonance) {
        this.resonance = resonance;
        this.mainConfigLoader = new ConfigLoader<>(
                ResonanceConfig.class,
                resonance.dataFolder().resolve("main.conf"),
                options -> options.header("Resonance Configuration")
        );
    }

    public void loadConfiguration() {
        try {
            this.mainConfig = mainConfigLoader.load();
            this.mainConfigLoader.save(mainConfig);
        } catch (ConfigurateException e) {
            throw new IllegalStateException("Failed to load config", e);
        }
    }

    public @NotNull ResonanceConfig mainConfig() {
        return mainConfig;
    }

}
