package net.thiccaxe.resonance.implementation.paper;

import net.thiccaxe.resonance.implementation.paper.logging.PaperLogger;
import net.thiccaxe.resonance.implementation.paper.scheduler.PaperScheduler;
import net.thiccaxe.resonance.plugin.logging.ResonanceLogger;
import net.thiccaxe.resonance.plugin.ResonancePlugin;
import net.thiccaxe.resonance.plugin.scheduler.ResonanceScheduler;
import net.thiccaxe.resonance.server.ResonanceServer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ResonancePaper extends JavaPlugin implements ResonancePlugin {
    private @NotNull final ResonanceLogger logger = new PaperLogger(getLogger());
    private @NotNull final PaperScheduler scheduler = new PaperScheduler(this);
    private @NotNull final ResonanceServer server = new ResonanceServer(this);

    @Override
    public void onEnable() {
        logger.info("Starting Resonance ...");
        server.start(Integer.parseInt(System.getProperty("resonance.port", String.valueOf(8888))));
        logger.info("Resonance Started!");
    }

    @Override
    public void onDisable() {
        logger.info("Stopping Resonance ...");
        server.stop();
        logger.info("Resonance Stopped!");
    }

    @Override
    public @NotNull ResonanceLogger logger() {
        return logger;
    }

    @Override
    public @NotNull ResonanceScheduler scheduler() {
        return scheduler;
    }
}
