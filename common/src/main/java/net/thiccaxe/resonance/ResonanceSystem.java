package net.thiccaxe.resonance;

import net.thiccaxe.resonance.feature.FeatureEnableException;
import net.thiccaxe.resonance.feature.Feature;
import net.thiccaxe.resonance.logging.ResonanceLogger;
import net.thiccaxe.resonance.plugin.logging.ResonanceLogger;
import net.thiccaxe.resonance.server.ResonanceServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class ResonanceSystem implements Feature {
    private final @NotNull String featureName = getClass().getSimpleName();
    private final @NotNull @Unmodifiable List<String> featureDescription = List.of("The base for all of Resonance,",
            "Contains various necessary subsystems");


    private boolean enabled = false;

    private final ResonanceLogger logger;
    private final ResonanceServer server;

    public ResonanceSystem(
            ResonanceLogger logger,
            ResonanceServer server
    ) {
        this.logger = logger;
        this.server = server;
    }


    @Override
    public void enable() throws FeatureEnableException {
        enableFeatures(
                server
        );
        enabled = true;
    }

    protected void enableFeatures(@NotNull Feature @NotNull... features)  {
        for (Feature feature : features) {
            try {
                feature.enable();
            } catch (FeatureEnableException e) {
                logger.warn("Failed to enable feature: (It may be necessary! - Check Below)" );
                logger.warn(feature.name());
                feature.description().forEach(logger::warn);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void disable() {
        disableFeatures(
                server
        );
        enabled = false;
    }

    protected void disableFeatures(@NotNull Feature @NotNull... features) {
        for (Feature feature : features) {
            try {
                feature.disable();
            } catch (Exception e) {
                logger.warn("Failed to disable feature:");
                logger.warn(feature.name());
                feature.description().forEach(logger::warn);
            }
        }
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public @NotNull String name() {
        return "ResonanceSystem";
    }

    @Override
    public @NotNull List<String> description() {
        return null;
    }
}
