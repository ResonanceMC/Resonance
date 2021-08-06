package net.thiccaxe.resonance.network;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class ConnectionManager {

    private static final long KEEP_ALIVE_DELAY = 10 * 1000; // millis
    private static final long KEEP_ALIVE_TIMEOUT = 10 * 3000; // millis


    private final Set<Object> users = new CopyOnWriteArraySet<>();
    private final Set<Object> unmodifiableUsers = Collections.unmodifiableSet(users);

}
