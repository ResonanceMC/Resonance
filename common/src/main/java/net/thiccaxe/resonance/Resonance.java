package net.thiccaxe.resonance;

import net.thiccaxe.resonance.auth.uuid.UuidAuth;
import net.thiccaxe.resonance.auth.uuid.UuidToken;
import net.thiccaxe.resonance.config.ConfigManager;
import net.thiccaxe.resonance.network.netty.NettyServer;
import net.thiccaxe.resonance.platform.Platform;
import net.thiccaxe.resonance.platform.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

public class Resonance {
    private final @NotNull Platform platform;
    private final @NotNull ConfigManager configManager;
    private final @NotNull UuidAuth authManager;
    private final @NotNull UuidAuth quickAuthManager;
    private final @NotNull NettyServer nettyServer;
    private final @NotNull Scheduler.Task serverTask;


    public Resonance(final @NotNull Platform platform) {
        this.platform = platform;
        this.configManager = new ConfigManager(this);
        configManager.loadConfiguration();
        this.authManager = new UuidAuth(uuid -> {
            StringBuilder token = new StringBuilder();
            for (int i = 0; i < 6; i ++) {
                token.append(ThreadLocalRandom.current().nextInt(0, 10));
            }
            return new UuidToken(token.toString(), uuid, System.currentTimeMillis() + 300);
        }, this, true);
        this.quickAuthManager = new UuidAuth(uuid -> {
            StringBuilder token = new StringBuilder();
            for (int i = 0; i < 6; i ++) {
                token.append(ThreadLocalRandom.current().nextInt(0, 10));
            }
            return new UuidToken(token.toString(), uuid, System.currentTimeMillis() + 300);
        }, this, false);

        this.nettyServer = new NettyServer(this);
        this.serverTask = this.platform.scheduler().scheduleAsyncTask(() -> {
            //packetProcessor.start();
            nettyServer.start(configManager.mainConfig().getPort());
        });
        //this.packetTask = this.platform.scheduler()
        //        .scheduleRepeatingTask(packetProcessor, 0, 1);

    }

    public void shutdown() {
        serverTask.cancel();
        nettyServer.stop();
        //packetProcessor.stop();
        //packetTask.cancel();
    }

    public @NotNull Platform platform() {
        return this.platform;
    }

    public @NotNull ConfigManager configManager() {
        return configManager;
    }

    public @NotNull UuidAuth authManager() {
        return authManager;
    }

    public @NotNull UuidAuth quickAuthManager() {
        return quickAuthManager;
    }

    public @NotNull Path dataFolder() {
        return platform.dataFolder();
    }
}
