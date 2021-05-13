package net.thiccaxe.resonance.model;

import org.jetbrains.annotations.Nullable;

public abstract class UserCachedData {
    private final User user;


    private long lastUpdateTime = 0;

    private @Nullable UserLocation location;



    public UserCachedData(User user) {
        this.user = user;
    }




}
