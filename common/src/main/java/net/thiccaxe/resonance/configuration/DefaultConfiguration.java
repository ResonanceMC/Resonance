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

import net.thiccaxe.resonance.logging.ResonanceLogger;
import net.thiccaxe.resonance.plugin.ResonancePlugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public abstract class DefaultConfiguration extends ResonanceConfigurationImpl<DefaultConfigValues> {
    private final DefaultConfigValues values = new DefaultConfigValues();

    protected DefaultConfiguration(@NotNull ResonancePlugin plugin, @NotNull File dataFolder) {
        super(plugin, dataFolder);
    }

    @Override
    protected void getConfigValues(@NotNull ConfigurationNode root) {
        values.setHost(new InetSocketAddress(root.node("port").getInt(8888)));
        values.setAddress(InetSocketAddress.createUnresolved(root.node("address").getString("localhost"), 80).getAddress());
    }


    @Override
    public DefaultConfigValues config() {
        return values;
    }

}
