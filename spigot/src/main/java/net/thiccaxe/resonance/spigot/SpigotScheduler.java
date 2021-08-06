package net.thiccaxe.resonance.spigot;

import net.thiccaxe.resonance.platform.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class SpigotScheduler implements Scheduler {
    private final ResonanceSpigot plugin;

    public SpigotScheduler(ResonanceSpigot plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task scheduleAsyncTask(Runnable runnable) {
        return new SpigotTask(
                Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable)
        );
    }

    @Override
    public Task scheduleTask(Runnable runnable) {
        return new SpigotTask(
                Bukkit.getScheduler().runTask(plugin, runnable)
        );
    }

    @Override
    public Task scheduleRepeatingTask(Runnable runnable, int delay, int period) {
        return new SpigotTask(
                Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0, period)
        );
    }

    public static class SpigotTask implements Task {
        private final BukkitTask task;

        public SpigotTask(BukkitTask task) {
            this.task = task;
        }

        @Override
        public void cancel() {
            this.task.cancel();
        }
    }
}
