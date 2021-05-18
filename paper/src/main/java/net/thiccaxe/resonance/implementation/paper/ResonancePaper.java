package net.thiccaxe.resonance.implementation.paper;

import net.thiccaxe.resonance.feature.FeatureEnableException;
import net.thiccaxe.resonance.feature.Feature;
import net.thiccaxe.resonance.implementation.paper.logging.PaperLogger;
import net.thiccaxe.resonance.implementation.paper.scheduler.PaperScheduler;
import net.thiccaxe.resonance.logging.ResonanceLogger;
import net.thiccaxe.resonance.plugin.ResonancePlugin;
import net.thiccaxe.resonance.plugin.scheduler.ResonanceScheduler;
import net.thiccaxe.resonance.server.ResonanceServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class ResonancePaper extends JavaPlugin implements ResonancePlugin {
    private boolean enabled = false;
    private @NotNull final ResonanceLogger logger = new PaperLogger(getLogger());
    private @NotNull final PaperScheduler scheduler = new PaperScheduler(this);
    private @NotNull final ResonanceServer server = new ResonanceServer(this, 8888);


    private @NotNull final @Unmodifiable List<Feature> features = List.of(
            scheduler,
            server
    );

    @Override
    public void onEnable() {
        try {
            enable();
        } catch (FeatureEnableException e) {
            logger.warn("Something bad has probably happened ...");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        disable();
    }

    @Override
    public @NotNull ResonanceLogger logger() {
        return logger;
    }

    @Override
    public @NotNull ResonanceScheduler scheduler() {
        return scheduler;
    }

    @Override
    public void enable() throws FeatureEnableException {
        if (enabled) return;


        enableFeatures(features);
        enabled = true;
    }

    @Override
    public void disable() {

        disableFeatures(features);
        enabled = false;
    }

    public void enableFeatures(List<Feature> features) {
        for (Feature feature : features) {
            try {
                feature.enable();
            } catch (FeatureEnableException e) {
                logger.warn("Check for the feature that errored during enabling ...");
                e.printStackTrace();
            }
        }
    }

    private void disableFeatures(List<Feature> features) {
        for (Feature feature : features) {
            try {
                feature.disable();
            } catch (Exception e) {
                logger.warn("Check for the feature that errored during disabling ...");
                e.printStackTrace();
            }
        }
    }



    @Override
    public boolean enabled() {
        return false;
    }
}
