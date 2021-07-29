package net.thiccaxe.resonance.platform;

public interface Scheduler {

    Task scheduleAsyncTask(Runnable runnable);

    Task scheduleTask(Runnable runnable);

    Task scheduleRepeatingTask(Runnable runnable, int delay, int period);

    interface Task {
        void cancel();
    }

}
