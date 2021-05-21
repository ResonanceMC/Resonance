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
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class User {

    private final @NotNull UUID uuid;

    // Last Known Username
    protected @Nullable String username;

    public User(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the user's uuid.
     * @return the uuid
     */
    public @NotNull UUID getUuid() {
        return uuid;
    }

    /**
     * Gets the username. Returns null if username is null.
     * @return the username
     */
    public @Nullable String getUsername() {
        return username;
    }

    /**
     * Gets the username. if null, returns uuid in string format.
     * @return the username or if null the uuid
     */
    public @NotNull String getName() {
        if (username != null) return username;
        return uuid.toString();
    }

    public abstract @NotNull UserCachedData getData();
    public abstract void setData(@NotNull UserCachedData data);



    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User that = (User) o;
        return this.uuid.equals(that.uuid);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + uuid + ")";
    }
}
