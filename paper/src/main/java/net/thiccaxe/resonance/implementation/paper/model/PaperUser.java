package net.thiccaxe.resonance.implementation.paper.model;

import net.thiccaxe.resonance.model.User;
import net.thiccaxe.resonance.model.UserCachedData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PaperUser extends User {
    private UserCachedData data;

    public PaperUser(@NotNull UUID uuid) {
        super(uuid);
    }

    @Override
    public @NotNull UserCachedData getData() {
        return data;
    }

    @Override
    public void setData(@NotNull UserCachedData data) {
        this.data = data;
    }
}
