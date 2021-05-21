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

package net.thiccaxe.resonance.platform.paper;

import net.thiccaxe.resonance.configuration.DefaultConfigValues;
import net.thiccaxe.resonance.configuration.ResonanceConfiguration;
import net.thiccaxe.resonance.feature.FeatureEnableException;
import net.thiccaxe.resonance.logging.ResonanceLogger;
import net.thiccaxe.resonance.platform.paper.configuration.PaperConfiguration;
import net.thiccaxe.resonance.platform.paper.logging.PaperLogger;
import net.thiccaxe.resonance.platform.paper.scheduling.PaperScheduler;
import net.thiccaxe.resonance.plugin.ResonancePlugin;
import net.thiccaxe.resonance.scheduling.ResonanceScheduler;
import net.thiccaxe.resonance.server.ResonanceServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ResonancePaper extends JavaPlugin implements ResonancePlugin {
    private boolean enabled = false;
    private @NotNull final ResonanceLogger logger = new PaperLogger(getLogger());
    private @NotNull final PaperScheduler scheduler = new PaperScheduler(this);
    private @NotNull final ResonanceServer server = new ResonanceServer(this, 8888);
    private @NotNull final PaperConfiguration configuration = new PaperConfiguration(this, getDataFolder());

    @Override
    public void onEnable() {
        try {
            enable();
        } catch (FeatureEnableException e) {
            logger.warn("Failed to enable!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        disable();
    }

    @Override
    public @NotNull ResonanceLogger logger() {
        return logger;
    }

    @Override
    public @NotNull ResonanceScheduler scheduler() {
        return scheduler;
    }

    @Override
    public @NotNull ResonanceConfiguration<DefaultConfigValues> configuration() {
        return configuration;
    }

    @Override
    public void enable() throws FeatureEnableException {
        if (enabled) return;

        scheduler.enable();
        configuration.enable();
        server.setPort(configuration.config().getHost().getPort());
        server.enable();
        enabled = true;
    }

    @Override
    public void disable() {
        if (!enabled) return;

        server.disable();
        configuration.disable();
        scheduler.disable();

        enabled = false;
    }

    @Override
    public boolean enabled() {
        return false;
    }
}
