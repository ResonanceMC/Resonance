package net.thiccaxe.resonance.implementation.paper.scheduler;

import net.thiccaxe.resonance.feature.FeatureEnableException;
import net.thiccaxe.resonance.implementation.paper.ResonancePaper;
import net.thiccaxe.resonance.plugin.scheduler.AbstractJavaScheduler;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;

public class PaperScheduler extends AbstractJavaScheduler {



    private final ResonancePaper plugin;


    public PaperScheduler(ResonancePaper plugin) {
        super();
        this.plugin = plugin;

    }

    @Override
    public Executor sync() {
        return Bukkit.getScheduler().getMainThreadExecutor(plugin);
    }

    @Override
    public void enable() throws FeatureEnableException {

    }

    @Override
    public void disable() {

    }

    @Override
    public boolean enabled() {
        return false;
    }
}
