package net.thiccaxe.resonance.feature;

/**
 * An exception thrown when a feature fails to enable.
 *
 * Inspired by Plan (https://github.com/plan-player-analytics/Plan).
 */
public class FeatureEnableException extends Exception {

    /**
     * Thrown when a {@link Feature} fails to enable.
     * @param message the exception message
     */
    public FeatureEnableException(String message) {
        super(message);
    }

    /**
     * Thrown when a {@link Feature} fails to enable.
     * @param message the exception message
     * @param cause the cause of the exception
     */
    public FeatureEnableException(String message, Throwable cause) {
        super(message, cause);
    }

}