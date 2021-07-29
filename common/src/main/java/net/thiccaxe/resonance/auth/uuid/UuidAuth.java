package net.thiccaxe.resonance.auth.uuid;

import net.thiccaxe.resonance.Resonance;
import net.thiccaxe.resonance.auth.AuthManager;
import org.spongepowered.configurate.ConfigurateException;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public class UuidAuth implements AuthManager<UUID, UuidToken, String> {

    private final Resonance resonance;
    private final Function<UUID, UuidToken> tokenGenerator;

    private static final long DEFAULT_EXPIRY_MS = 1000*20; // 20 Seconds

    private UuidStorage.BasicTokens tokens;
    private final boolean persist;

    private UuidStorage storage;

    public UuidAuth(Function<UUID, UuidToken> tokenGenerator, Resonance resonance, boolean persist) {
        this.resonance = resonance;
        this.tokenGenerator = tokenGenerator;
        this.persist = persist;
        try {
            if (persist) {
                this.storage = new UuidStorage(resonance.dataFolder().resolve("tokens.fac"));
                this.tokens = storage.loadTokens();
            } else {
                this.tokens = new UuidStorage.BasicTokens(new HashMap<>(), new HashMap<>());
            }
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateToken(UUID s) {
        generateToken(s, DEFAULT_EXPIRY_MS);
    }
    /*
    Will Automatically invalidate any old tokens!
     */
    @Override
    public void generateToken(UUID key, long expiry) {
        if (tokens.keyToTokenMap().containsKey(key)) {
            UuidToken token = tokens.keyToTokenMap().get(key);
            tokens.tokenValueToKeyMap().remove(token.getValue());
            tokens.keyToTokenMap().remove(key);
        }
        UuidToken token = tokenGenerator().apply(key);
        //System.out.println(token);
        tokens.keyToTokenMap().put(key, token);
        tokens.tokenValueToKeyMap().put(token.getValue(), key);
        reloadTokens();
        //mapState();
    }

    @Override
    public boolean isValid(UUID key, String token) {
        // Check if key is stored
        if (!tokens.keyToTokenMap().containsKey(key)) return false;

        UuidToken linkedToken = tokens.keyToTokenMap().get(key);

        //System.out.println(System.currentTimeMillis() + " > " + linkedToken.getExpiry() + " = " + (System.currentTimeMillis() > linkedToken.getExpiry()));
        // Check if token is expired
        if (System.currentTimeMillis() > linkedToken.getExpiry()) {
            tokens.keyToTokenMap().remove(key);
            tokens.tokenValueToKeyMap().remove(linkedToken.getValue());
            reloadTokens();
            return false;
        }

        // Check if token linked with key is same as supplied token
        if (!linkedToken.getValue().equals(token)) return false;

        //mapState();
        //System.out.println(linkedToken);

        // Double check   -required? YEP - atleast for debugging :eyes:
        if (!tokens.tokenValueToKeyMap().containsKey(linkedToken.getValue())) return false;
        if (!tokens.tokenValueToKeyMap().get(linkedToken.getValue()).equals(key)) return false;

        return true;
    }

    @Override
    public UuidToken getLinkedToken(UUID key) {
        // Check if key is stored
        if (!tokens.keyToTokenMap().containsKey(key)) return null;

        UuidToken linkedToken = tokens.keyToTokenMap().get(key);

        if (!isValid(key, linkedToken.getValue())) return null;

        return linkedToken;
    }

    @Override
    public UUID getLinkedKey(String token) {
        // Check if token is valid
        if (!tokens.tokenValueToKeyMap().containsKey(token)) return null;

        UUID linkedKey = tokens.tokenValueToKeyMap().get(token);
        UuidToken linkedToken = getLinkedToken(linkedKey);
        if (linkedToken == null) return null;
        return linkedKey;
    }

    @Override
    public void invalidateToken(String token) {
        UUID linkedKey = getLinkedKey(token);
        if (linkedKey == null) return;
        tokens.keyToTokenMap().remove(linkedKey);
        tokens.tokenValueToKeyMap().remove(token);
        reloadTokens();
    }

    @Override
    public Function<UUID, UuidToken> tokenGenerator() {
        return tokenGenerator;
    }

    void mapState() {
        System.out.println(tokens.keyToTokenMap().size());
        for (UUID key : tokens.keyToTokenMap().keySet()) {
            System.out.println(key + ": " + tokens.keyToTokenMap().get(key));
        }
        System.out.println(tokens.tokenValueToKeyMap().size());
        for (String key : tokens.tokenValueToKeyMap().keySet()) {
            System.out.println(key + ": " + tokens.tokenValueToKeyMap().get(key));
        }
    }

    void reloadTokens() {
        if (!persist) {
            return;
        }
        try {
            tokens = storage.reloadTokens(tokens);
        } catch (Exception e) {e.printStackTrace();}
    }
}