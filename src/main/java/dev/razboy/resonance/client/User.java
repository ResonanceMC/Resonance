package dev.razboy.resonance.client;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import dev.razboy.resonance.token.Token;

import java.util.Objects;
import java.util.UUID;

public class User {
    private final Token token;
    private boolean online;
    private Player player;

    private final JSONObject playerData;

    private double posX = 0.00d;
    private double posY = 0.00d;
    private double posZ = 0.00d;
    private double rotYaw = 90.00d;
    private double rotPitch = 0.00d;
    private double uposX = 0.00d;
    private double uposY = 0.00d;
    private double uposZ = 0.00d;
    private double urotYaw = 90.00d;
    private double urotPitch = 0.00d;
    private UUID world = UUID.randomUUID();
    private UUID uworld = UUID.randomUUID();
    public User(Token token, Player player) {
        online = player != null;
        this.player = online ? player : null;
        this.token = token;
        this.playerData = new JSONObject().put("uuid", token.uuid()).put("username", token.username());
        if (onlineUpdate()) {updateOnline();}
        if (positionUpdate()) {updatePosition();}
        if (worldUpdate()) {updateWorld();}
    }

    private JSONObject update() {
        player = Bukkit.getPlayer(UUID.fromString(token.uuid()));
        online = player != null;
        if (online) {
            Location l = player.getLocation();
            posX = round(l.getX());
            posY = round(l.getY());
            posZ = round(l.getZ());
            rotYaw = round(l.getYaw());
            rotPitch = round(l.getPitch());
            world = l.getWorld().getUID();
            return withData()
                            .put("online", online)
                            .put("pos", new JSONObject()
                                    .put("x", posX)
                                    .put("y", posY)
                                    .put("z", posZ)
                                    .put("rotation", new JSONArray(new double[]{rotYaw, rotPitch}))
                            )
                            .put("dimension", world.toString());
        }
        return new JSONObject()
                        .put("online", false);

    }

    private double round(double n) {
        return Math.round(n*100.0)/100.0;
    }

    public JSONObject getFullJson() {
        return update();
    }

    public Player getPlayer() {
        return player;
    }



    public boolean onlineUpdate() {
        return online == (Bukkit.getPlayer(UUID.fromString(token.uuid())) == null);
    }
    public JSONObject updateOnline() {
        online = Bukkit.getPlayer(UUID.fromString(token.uuid())) != null;
        return withData()
                        .put("online", online);
    }

    public boolean worldUpdate() {
        if (online) {
            Location l = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(token.uuid()))).getLocation();
            uworld = l.getWorld().getUID();
            return !uworld.equals(world);
        }
        return false;
    }
    public JSONObject updateWorld() {
        world = uworld;
        return withData()
                .put("dimension", world.toString());
    }

    public boolean positionUpdate() {
        if (online) {
            Location l = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(token.uuid()))).getLocation();
            l.getWorld().getUID();
            uposX = round(l.getX());
            uposY = round(l.getY());
            uposZ = round(l.getZ());
            urotYaw = round(l.getYaw());
            urotPitch = round(l.getPitch());
            return uposX != posX || uposY != posY || uposZ != posZ || urotPitch != rotPitch || urotYaw != rotYaw;
        }
        return false;
    }

    public JSONObject updatePosition() {
        final boolean xupdated = uposX != posX;
        final boolean yupdated = uposY != posY;
        final boolean zupdated = uposZ != posZ;
        final boolean rupdated = urotPitch != rotPitch || urotYaw != rotYaw;
        if (xupdated) {
            posX = uposX;
        }
        if (yupdated) {
            posY = uposY;
        }
        if (zupdated) {
            posZ = uposZ;
        }
        if (rupdated) {
            rotPitch = urotPitch;
            rotYaw = urotYaw;
        }
        return withData()
                        .put("pos", new JSONObject()
                                .put("x", xupdated ? posX:null)
                                .put("y", yupdated ? posY:null)
                                .put("z", zupdated ? posZ:null)
                                .put("rotation", rupdated ? new JSONArray(new double[]{rotYaw, rotPitch}):null)
                        );
    }
    public JSONObject withData() {
        return new JSONObject().put("data", playerData);
    }

    public boolean isOnline() {
        return online;
    }
}

