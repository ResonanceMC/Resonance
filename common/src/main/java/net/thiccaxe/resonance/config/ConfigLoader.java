/*
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

import java.nio.file.Path;
import java.util.function.UnaryOperator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

public final class ConfigLoader<C> {
    private static final TypeSerializerCollection SERIALIZERS;

    static {
        SERIALIZERS = TypeSerializerCollection.defaults();
    }

    private final HoconConfigurationLoader loader;
    private final ObjectMapper<C> mapper;

    public ConfigLoader(
            final @NonNull Class<C> configClass,
            final @NonNull Path configPath,
            final @NonNull ConfigurationOptions options
    ) {
        this.loader = HoconConfigurationLoader.builder()
                .path(configPath)
                .defaultOptions(options.serializers(SERIALIZERS))
                .build();
        try {
            this.mapper = ObjectMapper.factory().get(configClass);
        } catch (final SerializationException ex) {
            throw new IllegalStateException(
                    "Failed to initialize an object mapper for type: " + configClass.getSimpleName(),
                    ex
            );
        }
    }

    public ConfigLoader(
            final @NonNull Class<C> configClass,
            final @NonNull Path configPath,
            final @NonNull UnaryOperator<ConfigurationOptions> options
    ) {
        this(configClass, configPath, options.apply(ConfigurationOptions.defaults()));
    }

    public ConfigLoader(
            final @NonNull Class<C> configClass,
            final @NonNull Path configPath
    ) {
        this(configClass, configPath, ConfigurationOptions.defaults());
    }

    public @NonNull C load() throws ConfigurateException {
        final CommentedConfigurationNode node = this.loader.load();
        return this.mapper.load(node);
    }

    public void save(final @NonNull C config) throws ConfigurateException {
        final CommentedConfigurationNode node = this.loader.createNode();
        this.mapper.save(config, node);
        this.loader.save(node);
    }
}