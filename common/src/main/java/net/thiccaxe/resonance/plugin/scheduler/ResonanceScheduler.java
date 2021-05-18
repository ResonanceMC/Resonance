package net.thiccaxe.resonance.plugin.scheduler;

import net.thiccaxe.resonance.feature.Feature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public interface ResonanceScheduler extends Feature {
    @NotNull String featureName = "ResonanceScheduler";
    @NotNull @Unmodifiable List<String> featureDescription = List.of(
            "Handles tasks that need to be run async or sync,",
            "On repeat or once."
    );

    /**
     * Gets an async executor instance
     *
     * @return an async executor instance
     */
    Executor async();

    /**
     * Gets a sync executor instance
     *
     * @return a sync executor instance
     */
    Executor sync();

    /**
     * Executes a task async
     *
     * @param task the task
     */
    default void executeAsync(Runnable task) {
        async().execute(task);
    }

    /**
     * Executes a task sync
     *
     * @param task the task
     */
    default void executeSync(Runnable task) {
        sync().execute(task);
    }

    /**
     * Executes the given task with a delay.
     *
     * @param task the task
     * @param delay the delay
     * @param unit the unit of delay
     * @return the resultant task instance
     */
    SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit);

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task the task
     * @param interval the interval
     * @param unit the unit of interval
     * @return the resultant task instance
     */
    SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit);

    /**
     * Executes the given task with a delay.
     *
     * @param task the task
     * @param delay the delay
     * @param unit the unit of delay
     * @return the resultant task instance
     */
    SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit);

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task the task
     * @param interval the interval
     * @param unit the unit of interval
     * @return the resultant task instance
     */
    SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit);


    /**
     * Shuts down the scheduler instance.
     *
     * <p>{@link #asyncLater(Runnable, long, TimeUnit)} and {@link #asyncRepeating(Runnable, long, TimeUnit)}.</p>
     */
    void shutdownScheduler();

    /**
     * Shuts down the executor instance.
     *
     * <p>{@link #async()} and {@link #executeAsync(Runnable)}.</p>
     */
    void shutdownExecutor();

    @Override
    default @NotNull String name() {
        return featureName;
    }

    @Override
    default @NotNull @Unmodifiable List<String> description() {
        return featureDescription;
    }
}
