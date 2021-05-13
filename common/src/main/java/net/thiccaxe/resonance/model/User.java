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
