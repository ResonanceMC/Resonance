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

package net.thiccaxe.resonance.model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserLocation {
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final UserLocation.World world;

    public UserLocation(final UserLocation.World world, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public UserLocation.World getWorld() {
        return world;
    }

    public static class World {
        private final @NotNull UUID uuid;

        public World(@NotNull UUID uuid) {
            this.uuid = uuid;
        }

        public @NotNull UUID getUuid() {
            return uuid;
        }

        @Override
        public int hashCode() {
            return uuid.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof UserLocation.World)) return false;
            final UserLocation.World that = (UserLocation.World) o;
            return this.uuid.equals(that.uuid);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" + uuid + ")";
        }
    }
}
