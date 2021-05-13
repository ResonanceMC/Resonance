package net.thiccaxe.resonance.plugin.logging;

import org.jetbrains.annotations.NotNull;

public interface ResonanceLogger {

    void info(@NotNull String message);

    void warn(@NotNull String message);

    void error(@NotNull String message);


    @NotNull String prefix();
}
