package net.thiccaxe.resonance.auth;

import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Map;

public interface TokenStorage<KEY, TOKEN extends Token<KEY, S>, S> {

    void saveTokens(Tokens<KEY, TOKEN, S> tokens) throws Exception;

    Tokens<KEY, TOKEN, S> loadTokens() throws Exception;

    Tokens<KEY, TOKEN, S> reloadTokens(Tokens<KEY, TOKEN, S> tokens) throws Exception;

    interface Tokens<KEY, TOKEN extends Token<KEY, S>, S> {
        Map<KEY, TOKEN> keyToTokenMap();
        Map<S, KEY> tokenValueToKeyMap();
    }
}
