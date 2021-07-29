package net.thiccaxe.resonance.auth.basic;

import net.thiccaxe.resonance.auth.Token;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class StringToken implements Token<String, String> {


    protected long expiry;
    protected String value;
    protected String key;

    public StringToken(){
        this.expiry = 0;
        this.value = null;
        this.key = null;
    }

    public StringToken(String value, String key, long expiry) {
        this.value = value;
        this.key = key;
        this.expiry = expiry;
    }

    public long getExpiry() {
        return expiry;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return String.format("%s[key=%s,value=%s,expiry=%s]", getClass().getSimpleName(), key, value, expiry);
    }
}
