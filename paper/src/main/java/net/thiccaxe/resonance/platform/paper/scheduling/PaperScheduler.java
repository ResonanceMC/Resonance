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

package net.thiccaxe.resonance.platform.paper.scheduling;

import net.thiccaxe.resonance.logging.ResonanceLogger;
import net.thiccaxe.resonance.platform.paper.ResonancePaper;
import net.thiccaxe.resonance.scheduling.AbstractJavaScheduler;
import net.thiccaxe.resonance.scheduling.SchedulerTask;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class PaperScheduler extends AbstractJavaScheduler {
    private boolean enabled = false;


    private final ResonancePaper plugin;


    public PaperScheduler(ResonancePaper plugin) {
        super();
        this.plugin = plugin;

    }

    @Override
    public Executor sync() {
        return Bukkit.getScheduler().getMainThreadExecutor(plugin);
    }

    @Override
    public SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit) {
        return null;
    }

    @Override
    public SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit) {
        return null;
    }

    @Override
    public void enable()  {

    }

    @Override
    public void disable() {

    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    public @NotNull ResonanceLogger logger() {
        return plugin.logger();
    }
}
