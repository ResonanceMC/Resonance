package net.thiccaxe.resonance.feature;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Represents a feature that may be enabled or disabled.
 *
 * Inspired by Plan (https://github.com/plan-player-analytics/Plan).
 */
public interface ResonanceFeature {

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
     * Return the name of the feature.
     *
     * @return the name
     */
    @NotNull String name();

    /**
     * Return a description of the feature, separated into lines if necessary,
     *
     * @return the description.
     */
    @NotNull @Unmodifiable List<String> description();
}
