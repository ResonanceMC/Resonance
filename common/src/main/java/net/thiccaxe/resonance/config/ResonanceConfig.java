package net.thiccaxe.resonance.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.function.Supplier;

@ConfigSerializable
public class ResonanceConfig {

    public final static int DEFAULT_PORT = 42069;
    public final static String DEFAULT_HOST = "localhost";
    private final static Supplier<String> keyProvider = () -> {
        final Random random = new SecureRandom();
        final byte[] key = new byte[128];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    };

    public ResonanceConfig() {
        this (DEFAULT_PORT, DEFAULT_HOST, keyProvider.get());
    }

    public ResonanceConfig(int port, String host, String key) {
        this.port = port;
        this.host = host;
        this.key = key;
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

    @Comment("The private key! KEEP THIS SECURE! Changing will invalidate all sessions.")
    private final String key;

    public String getKey() {
        return key;
    }

}
