package dev.razboy.resonance.token;

import com.google.common.collect.HashBiMap;
import dev.razboy.resonance.Resonance;
import dev.razboy.resonance.config.impl.TokenConfig;
import dev.razboy.resonance.network.Connection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class TokenManager {
    private final Resonance plugin;
    private final TokenConfig tokenConfig;
    private final HashBiMap<Token, String> tokens = HashBiMap.create();
    private final HashBiMap<String, Token> tokenStrings = HashBiMap.create();
    private final HashBiMap<Token, String> authTokens = HashBiMap.create();
    private final HashBiMap<String, Token> authTokenStrings = HashBiMap.create();
    public TokenManager(Resonance instance, TokenConfig tokenConfig) {
        plugin=instance;
        this.tokenConfig = tokenConfig;
        loadTokens();
    }

    public void loadTokens() {
        tokens.clear();
        tokenStrings.clear();
        tokens.putAll(tokenConfig.getTokens());
        tokens.forEach((token, uuid) -> tokenStrings.forcePut(token.token(), token));
    }

    public void registerToken(Token token) {
        //System.out.println(token + token.uuid());
        tokens.forcePut(token, token.uuid());
        tokenStrings.forcePut(token.token(), token);
        tokenConfig.saveTokens(tokens);

    }
    public Token generateAuthToken(Player player) {
        return generateAuthToken(player.getUniqueId().toString(), player.getName());
    }
    public Token generateAuthToken(String uuid, String username) {
        Token token = new Token(uuid, username, false);
        return removeIfContains(uuid, token);
    }

    public Token generateAuthToken(String uuid, String username, String tokenString) {
        Token token = new Token(uuid, username, false, tokenString);
        return removeIfContains(uuid, token);
    }

    @NotNull
    private Token removeIfContains(String uuid, Token token) {
        if (authTokens.containsValue(uuid)) {
            if (authTokenStrings.containsValue(authTokens.inverse().get(uuid))) {
                authTokenStrings.remove(authTokens.inverse().get(uuid).token());
            }
        }
        authTokens.forcePut(token, uuid);
        authTokenStrings.forcePut(token.token(), token);
        return token;
    }

    public Token validateAuthToken(String tokenString) {
        if (tokenString == null) {return null;}
        try {
            if (authTokenStrings.containsKey(tokenString)) {
                Token token = authTokenStrings.get(tokenString);
                if (token != null) {
                    if (!token.token().equalsIgnoreCase("ABCDEF")) {
                        authTokens.remove(token);
                        authTokenStrings.remove(tokenString);
                    }
                    token = new Token(token.uuid(), token.username(), true);
                    registerToken(token);
                    return token;
                }
            }
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }


    public static String generateToken(int length, String chars) {
        StringBuilder token = new StringBuilder();
        for (int i=0; i<length;i++) {
            token.append(chars.charAt(ThreadLocalRandom.current().nextInt(0, chars.length())));
        }
        return token.toString();
    }

    public String listTokens() {
        StringBuilder message = new StringBuilder("Tokens:");
        tokens.forEach((token, uuid) -> {
            message.append("\n").append(token.token()).append(",\n ");});
        return message.toString();
    }

    public Token validateToken(String tokenString) {
        if (tokenString == null) {return null;}
        try {
            //System.out.println(tokenStrings.containsKey(tokenString));
            if (tokenStrings.containsKey(tokenString)) {
                //System.out.println(tokenStrings.get(tokenString));
                return tokenStrings.get(tokenString);
            }
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    public void invalidateToken(Token token) {
        tokens.remove(token);
        tokenStrings.remove(token.uuid());
        tokenConfig.saveTokens(tokens);
    }
}
