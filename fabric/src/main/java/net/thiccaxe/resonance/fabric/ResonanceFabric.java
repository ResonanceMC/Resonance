package net.thiccaxe.resonance.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.thiccaxe.resonance.Resonance;
import net.thiccaxe.resonance.platform.Platform;
import net.thiccaxe.resonance.platform.Scheduler;
import net.thiccaxe.resonance.platform.logger.JavaLogger;

import java.nio.file.Path;

public class ResonanceFabric implements ModInitializer, Platform {
    private Resonance resonance;
    private Path dataFolder;
    private JavaLogger logger;

    private FabricScheduler scheduler;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.dataFolder = FabricLoader.getInstance().getConfigDir().resolve(name());
            this.logger = new JavaLogger(java.util.logging.Logger.getLogger(name()));
            logger().info(name() + " v" + version());
            this.resonance = new Resonance(this);
        });
        this.scheduler = new FabricScheduler();
    }

    @Override
    public Path dataFolder() {
        return dataFolder;
    }

    @Override
    public Resonance getResonance() {
        return resonance;
    }

    @Override
    public JavaLogger logger() {
        return logger;
    }

    @Override
    public String version() {
        return "1.0.0";
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }
}
