package net.thiccaxe.resonance.platform;

public interface Scheduler {

    Task scheduleAsyncTask(Runnable runnable);

    Task scheduleTask(Runnable runnable);

    interface Task {
        void cancel();
    }

}
