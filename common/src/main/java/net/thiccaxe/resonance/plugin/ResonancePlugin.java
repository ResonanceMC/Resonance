package net.thiccaxe.resonance.plugin;

import net.thiccaxe.resonance.feature.ResonanceFeature;
import net.thiccaxe.resonance.plugin.logging.ResonanceLogger;
import net.thiccaxe.resonance.plugin.scheduler.ResonanceScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface ResonancePlugin extends ResonanceFeature {
    @NotNull String featureName = "Resonance";
    @NotNull @Unmodifiable List<String> featureDescription = List.of(
            "The Plugin itself. Handles pretty much everything",
            "* necessary *  :/ "
    );

    @NotNull ResonanceLogger logger();

    @NotNull ResonanceScheduler scheduler();


    @Override
    default @NotNull String name() {
        return featureName;
    }

    @Override
    default @NotNull @Unmodifiable List<String> description() {
        return featureDescription;
    }
}
