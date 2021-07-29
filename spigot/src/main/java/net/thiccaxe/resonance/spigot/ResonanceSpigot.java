package net.thiccaxe.resonance.spigot;

import net.thiccaxe.resonance.Resonance;
import net.thiccaxe.resonance.platform.Platform;
import net.thiccaxe.resonance.platform.Scheduler;
import net.thiccaxe.resonance.platform.logger.JavaLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.UUID;

public class ResonanceSpigot extends JavaPlugin implements Platform {
    private Resonance resonance;
    private JavaLogger logger;
    private SpigotScheduler scheduler;

    @Override
    public void onEnable() {
        this.logger = new JavaLogger(getLogger());
        logger().info(name() + " v" + version());
        this.scheduler = new SpigotScheduler(this);
        this.resonance = new Resonance(this);
        final UUID uuid = UUID.randomUUID();
        try {
            resonance.authManager().generateToken(uuid);
            getLogger().info(resonance.authManager().getLinkedToken(uuid).toString());
            resonance.authManager().invalidateToken(resonance.authManager().getLinkedToken(uuid).getValue());
            getLogger().info(resonance.authManager().getLinkedToken(uuid).toString());
        } catch (Exception ignored){}
    }

    @Override
    public void onDisable() {
        resonance.shutdown();
    }

    @Override
    public Path dataFolder() {
        return getDataFolder().toPath();
    }

    @Override
    public Resonance getResonance() {
        return resonance;
    }

    @Override
    public JavaLogger logger() {
        return logger;
    }

    @Override
    public String version() {
        return "1.0.0";
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }
}
