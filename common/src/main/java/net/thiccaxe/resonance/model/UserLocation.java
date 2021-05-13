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

    public class World {
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
