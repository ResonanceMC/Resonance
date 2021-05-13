package net.thiccaxe.resonance.plugin;

import net.thiccaxe.resonance.plugin.logging.ResonanceLogger;
import net.thiccaxe.resonance.plugin.scheduler.ResonanceScheduler;
import org.jetbrains.annotations.NotNull;

public interface ResonancePlugin {


    @NotNull ResonanceLogger logger();

    @NotNull ResonanceScheduler scheduler();


}
