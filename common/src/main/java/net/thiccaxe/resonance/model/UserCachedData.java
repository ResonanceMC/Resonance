package net.thiccaxe.resonance.model;

import org.jetbrains.annotations.Nullable;

public abstract class UserCachedData {
    protected final User user;
    protected @Nullable UserLocation location;


    private long lastUpdateTime = 0;




    public UserCachedData(User user) {
        this.user = user;
    }




}
