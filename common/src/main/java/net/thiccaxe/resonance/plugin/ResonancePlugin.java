package net.thiccaxe.resonance.plugin;

import net.thiccaxe.resonance.feature.Feature;
import net.thiccaxe.resonance.logging.ResonanceLogger;
import net.thiccaxe.resonance.plugin.scheduler.ResonanceScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface ResonancePlugin extends Feature {
    @NotNull String featureName = "Resonance";
    @NotNull @Unmodifiable List<String> featureDescription = List.of(
            "The Plugin itself. Handles pretty much everything",
            "* necessary *  :/ "
    );

    @NotNull ResonanceLogger logger();

    @NotNull ResonanceScheduler scheduler();

}
