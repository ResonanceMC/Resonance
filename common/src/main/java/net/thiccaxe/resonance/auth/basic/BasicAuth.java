package net.thiccaxe.resonance.auth.basic;

import net.thiccaxe.resonance.auth.AuthManager;
import org.spongepowered.configurate.ConfigurateException;

import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

public class BasicAuth implements AuthManager<String, StringToken, String> {

    private final Function<String, StringToken> tokenGenerator;

    private static final long DEFAULT_EXPIRY_MS = 1000*20; // 20 Seconds


    private final Map<String, StringToken> keyToTokenMap = new HashMap<>();
    private final Map<String, String> tokenValueToKeyMap = new HashMap<>();
    private BasicStorage.BasicTokens tokens;
    private final boolean persist;

    private BasicStorage storage;
    
    public BasicAuth(Function<String, StringToken> tokenGenerator, boolean persist) {
        this.tokenGenerator = tokenGenerator;
        this.persist = persist;
        try {
            if (persist) {
                this.storage = new BasicStorage(Path.of("./tokens.txt"));
                this.tokens = storage.loadTokens();
            } else {
                this.tokens = new BasicStorage.BasicTokens(new HashMap<>(), new HashMap<>());
            }
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateToken(String s) {
        generateToken(s, DEFAULT_EXPIRY_MS);
    }
    /*
    Will Automatically invalidate any old tokens!
     */
    @Override
    public void generateToken(String key, long expiry) {
        if (tokens.keyToTokenMap().containsKey(key)) {
            StringToken token = tokens.keyToTokenMap().get(key);
            tokens.tokenValueToKeyMap().remove(token.getValue());
            tokens.keyToTokenMap().remove(key);
        }
        StringToken token = tokenGenerator().apply(key);
        //System.out.println(token);
        tokens.keyToTokenMap().put(key, token);
        tokens.tokenValueToKeyMap().put(token.getValue(), key);
        reloadTokens();
        //mapState();
    }

    @Override
    public boolean isValid(String key, String token) {
        // Check if key is stored
        if (!tokens.keyToTokenMap().containsKey(key)) return false;

        StringToken linkedToken = tokens.keyToTokenMap().get(key);

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
    public StringToken getLinkedToken(String key) {
        // Check if key is stored
        if (!tokens.keyToTokenMap().containsKey(key)) return null;

        StringToken linkedToken = tokens.keyToTokenMap().get(key);

        if (!isValid(key, linkedToken.getValue())) return null;

        return linkedToken;
    }

    @Override
    public String getLinkedKey(String token) {
        // Check if token is valid
        if (!tokens.tokenValueToKeyMap().containsKey(token)) return null;

        String linkedKey = tokens.tokenValueToKeyMap().get(token);
        StringToken linkedToken = getLinkedToken(linkedKey);
        if (linkedToken == null) return null;
        return linkedKey;
    }

    @Override
    public void invalidateToken(String token) {
        String linkedKey = getLinkedKey(token);
        if (linkedKey == null) return;
        tokens.keyToTokenMap().remove(linkedKey);
        tokens.tokenValueToKeyMap().remove(token);
        reloadTokens();
    }

    @Override
    public Function<String, StringToken> tokenGenerator() {
        return tokenGenerator;
    }

    void mapState() {
        System.out.println(tokens.keyToTokenMap().size());
        for (String key : tokens.keyToTokenMap().keySet()) {
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

    public static void main(String[] args) {
        final Random random = new SecureRandom();
        final char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".toCharArray();
        final int tokenLength = 10;
        BasicAuth auth = new BasicAuth((key) -> {
            StringBuilder token = new StringBuilder();
            for (int i = 0; i < tokenLength; i++) {
                token.append(characters[random.nextInt(characters.length)]);
            }
            return new StringToken(token.toString(), key, System.currentTimeMillis() + DEFAULT_EXPIRY_MS);
        }, true);
        while (true) {
            Scanner in = new Scanner(System.in);
            String inp = in.nextLine();
            if (inp.startsWith("n")) {
                auth.generateToken(inp.substring(1));
                System.out.println(auth.getLinkedToken(inp.substring(1)));
            } else if (inp.startsWith("t")) {
                String key = auth.getLinkedKey(inp.substring(1));
                if (key == null) {
                    System.out.println(false);
                } else {
                    System.out.println(true + ": " + key);
                }
            } else if (inp.startsWith("b")) {
                break;
            }
            //auth.mapState();
        }
    }
}
