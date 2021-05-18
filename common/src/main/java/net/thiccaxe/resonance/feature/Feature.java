package net.thiccaxe.resonance.feature;


import net.thiccaxe.resonance.logging.ResonanceLogger;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a feature that may be enabled or disabled.
 *
 * Inspired by Plan (https://github.com/plan-player-analytics/Plan).
 */
public interface Feature {

    /**
     * Enable the feature.
     *
     * Throws an {@link FeatureEnableException} if the enabling fails
     */
    void enable() throws FeatureEnableException;

    /**
     * Disable the feature.
     */
    void disable();

    /**
     * Returns whether the feature is enabled.
     *
     * @return whether the feature is enabled
     */
    boolean enabled();

    /**
     * Returns a logger specific to this Feature.
     * May be the plugin logger.
     */
    @NotNull ResonanceLogger logger();
}
