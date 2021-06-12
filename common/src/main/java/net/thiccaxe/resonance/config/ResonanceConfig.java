package net.thiccaxe.resonance.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class ResonanceConfig {

    public final static int DEFAULT_PORT = 42069;
    public final static String DEFAULT_HOST = "localhost";

    public ResonanceConfig() {
        this (DEFAULT_PORT, DEFAULT_HOST);
    }

    public ResonanceConfig(int port, String host) {
        this.port = port;
        this.host = host;
    }

    @Comment("The port the webserver will run on\nDefault 42069")
    private final int port;

    public int getPort() {
        return port;
    }


    @Comment("The address that users will connect to the server with. (ie. voice.example.com)\nDefault \"localhost\"")
    private final String host;

    public String getHost() {
        return host;
    }


}
