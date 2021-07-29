package net.thiccaxe.resonance.auth;

public interface Token<KEY, S> {

    KEY getKey();

    S getValue();

    long getExpiry();
}
