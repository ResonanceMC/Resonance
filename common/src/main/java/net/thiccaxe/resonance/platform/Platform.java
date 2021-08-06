package net.thiccaxe.resonance.platform;

import net.thiccaxe.resonance.Resonance;
import net.thiccaxe.resonance.platform.logger.Logger;

import java.nio.file.Path;

public interface Platform {

    Path dataFolder();

    Resonance getResonance();

    Logger<?> logger();

    default String version() {
        return "1.0.0";
    }

    default String name() {
        return "Resonance";
    }

    Scheduler scheduler();

    /*
    Bukkit needs some temp. classloader replacement with javalin :/
     */
    default boolean useJavalinClassLoaderHack() {
        return false;
    }

    default ClassLoader classLoaderForJavalinHack() {
        return null;
    }
}
