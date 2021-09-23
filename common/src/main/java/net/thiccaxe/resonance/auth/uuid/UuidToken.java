package net.thiccaxe.resonance.auth.uuid;

import net.thiccaxe.resonance.auth.Token;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.UUID;

@ConfigSerializable
public class UuidToken implements Token<UUID, String> {

    protected long expiry;
    protected String value;
    protected UUID key;

    @Override
    public UUID getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
    @Override
    public long getExpiry() {
        return expiry;
    }

    public UuidToken() {
        this.expiry = 0;
        this.value = null;
        this.key = null;
    }



    public UuidToken(String value, UUID key, long expiry) {
        this.value = value;
        this.key = key;
        this.expiry = expiry;
    }

    @Override
    public String toString() {
        return String.format("%s[key=%s,value=%s,expiry=%s]", getClass().getSimpleName(), key, value, expiry);
    }
}
