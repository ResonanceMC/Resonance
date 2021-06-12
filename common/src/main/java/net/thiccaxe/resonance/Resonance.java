package net.thiccaxe.resonance;

import net.thiccaxe.resonance.config.ConfigManager;
import net.thiccaxe.resonance.network.PacketProcessor;
import net.thiccaxe.resonance.network.netty.NettyServer;
import net.thiccaxe.resonance.platform.Platform;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class Resonance {
    private final @NotNull Platform platform;
    private final @NotNull ConfigManager configManager;
    private final @NotNull NettyServer nettyServer;
    private final @NotNull PacketProcessor packetProcessor = new PacketProcessor();

    public Resonance(final @NotNull Platform platform) {
        this.platform = platform;
        this.configManager = new ConfigManager(this);
        configManager.loadConfiguration();
        this.nettyServer = new NettyServer(packetProcessor);
        this.platform.scheduler().scheduleAsyncTask(() -> {
            nettyServer.init();
            nettyServer.start(configManager.mainConfig().getPort());
        });

    }


    public @NotNull Platform platform() {
        return this.platform;
    }

    public @NotNull Path dataFolder() {
        return platform.dataFolder();
    }
}
