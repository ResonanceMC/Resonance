package net.thiccaxe.resonance.auth;

import java.util.function.Function;

public interface AuthManager<KEY, TOKEN extends Token<KEY, S>, S> {

    void generateToken(KEY key);

    void generateToken(KEY key, long expiry);

    boolean isValid(KEY key, S token);

    TOKEN getLinkedToken(KEY key);

    KEY getLinkedKey(S token);

    void invalidateToken(S token);

    Function<KEY, TOKEN> tokenGenerator();

    default void test() {
    }
}
