package net.thiccaxe.resonance.fabric;

import net.thiccaxe.resonance.platform.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FabricScheduler implements Scheduler {
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);


    @Override
    public Task scheduleAsyncTask(Runnable runnable) {
        return new FabricTask(
                executorService.submit(runnable)
        );
    }

    @Override
    public Task scheduleTask(Runnable runnable) {
        return null;
    }

    public static class FabricTask implements Task {
        private final Future<?> result;

        public FabricTask(Future<?> result) {
            this.result = result;
        }

        @Override
        public void cancel() {
            result.cancel(true);
        }
    }
}
