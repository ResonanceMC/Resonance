package net.thiccaxe.resonance.logging;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;

public interface ResonanceLogger extends Audience {

    void info(@NotNull String message);

    void info(@NotNull ComponentLike message);

    void warn(@NotNull String message);

    void warn(@NotNull ComponentLike message);

    void error(@NotNull String message);

    void error(@NotNull ComponentLike message);


    @NotNull String prefix();

}
