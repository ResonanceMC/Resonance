package net.thiccaxe.resonance.implementation.paper.logging;

import net.thiccaxe.resonance.plugin.logging.ResonanceLogger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class PaperLogger implements ResonanceLogger {
    private final Logger logger;

    public PaperLogger(Logger logger) {
        this.logger = logger;
    }


    @Override
    public void info(@NotNull String message) {
        logger.info(message);
    }

    @Override
    public void warn(@NotNull String message) {

    }

    @Override
    public void error(@NotNull String message) {

    }

    @Override
    public @NotNull String prefix() {
        return logger.getName();
    }
}
