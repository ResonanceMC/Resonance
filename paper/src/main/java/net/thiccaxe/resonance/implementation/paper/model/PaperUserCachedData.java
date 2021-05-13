package net.thiccaxe.resonance.implementation.paper.model;

import net.thiccaxe.resonance.model.CachedField;
import net.thiccaxe.resonance.model.User;
import net.thiccaxe.resonance.model.UserCachedData;
import net.thiccaxe.resonance.model.UserLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class PaperUserCachedData extends UserCachedData {

    public static UserLocation.World fromBukkit(World world) {
        return new UserLocation.World(world.getUID());
    }

    public static UserLocation fromBukkit(Location location) {
        return new UserLocation(fromBukkit(location.getWorld()),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
                );
    }

    private final CachedField<UserLocation, User> location = new CachedField<>(
            50,
            null,
            (user) -> fromBukkit(Bukkit.getPlayer(user.getUuid()).getLocation()));

    public PaperUserCachedData(User user) {
        super(user);
    }

    public UserLocation getLocation() {
        return location.get(user);
    }



}
