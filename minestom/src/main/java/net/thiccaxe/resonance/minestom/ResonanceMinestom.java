package net.thiccaxe.resonance.minestom;

import net.minestom.server.extensions.Extension;
import net.thiccaxe.resonance.Resonance;
import net.thiccaxe.resonance.platform.Platform;
import net.thiccaxe.resonance.platform.Scheduler;
import net.thiccaxe.resonance.platform.logger.Slf4jLogger;
import net.thiccaxe.resonance.platform.logger.SystemLogger;

import java.nio.file.Path;

public class ResonanceMinestom extends Extension implements Platform {
    private Resonance resonance;
    private SystemLogger logger;
    private MinestomScheduler scheduler;

    @Override
    public void initialize() {
        //this.logger = new Slf4jLogger(getLogger()); // old sabre
        this.logger = new SystemLogger(name());
        logger().info(name() + " v" + version());
        this.scheduler = new MinestomScheduler();
        this.resonance = new Resonance(this);
    }

    @Override
    public void terminate() {
        resonance.shutdown();
    }

    @Override
    public Path dataFolder() {
        return getDataDirectory();
    }

    @Override
    public Resonance getResonance() {
        return resonance;
    }

    @Override
    public SystemLogger logger() {
        return logger;
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public boolean useJavalinClassLoaderHack() {
        return true;
    }

    @Override
    public ClassLoader classLoaderForJavalinHack() {
        return ResonanceMinestom.class.getClassLoader();
    }
}
