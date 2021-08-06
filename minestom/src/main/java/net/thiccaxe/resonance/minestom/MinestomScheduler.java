package net.thiccaxe.resonance.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.utils.time.TimeUnit;
import net.thiccaxe.resonance.platform.Scheduler;

public class MinestomScheduler implements Scheduler {
    @Override
    public Scheduler.Task scheduleAsyncTask(Runnable runnable) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(runnable).schedule());
    }

    @Override
    public Scheduler.Task scheduleTask(Runnable runnable) {
        return scheduleAsyncTask(runnable);
    }

    @Override
    public Scheduler.Task scheduleRepeatingTask(Runnable runnable, int delay, int period) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(runnable)
                .delay(delay, TimeUnit.MILLISECOND)
                .repeat(period, TimeUnit.MILLISECOND)
                .schedule()
        );
    }

    public static class MinestomTask implements Scheduler.Task {
        private final net.minestom.server.timer.Task task;

        public MinestomTask(net.minestom.server.timer.Task task) {
            this.task = task;
        }

        @Override
        public void cancel() {
            this.task.cancel();
        }
    }
}
