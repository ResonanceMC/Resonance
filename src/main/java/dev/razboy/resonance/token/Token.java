package dev.razboy.resonance.token;

public class Token {
    private static final int SHORT_LEN = 6;
    private static final String SHORT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int FULL_LEN = 64;
    private static final String FULL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";


    private final String tokenString;
    private final long creationTime;
    private final String usernameString;
    private final String uuidString;
    private final boolean tokenType;

    public Token(String token, long creation, String uuid, String username, boolean type) {
        tokenString = token;
        creationTime = creation;
        usernameString = username;
        uuidString = uuid;
        tokenType = type;
    }
    public Token(String uuid, String username, boolean type) {
        tokenString = TokenManager.generateToken(type ? FULL_LEN : SHORT_LEN, type ? FULL_CHARS : SHORT_CHARS);
        creationTime = System.currentTimeMillis();
        usernameString = username;
        uuidString = uuid;
        tokenType = type;
    }

    public Token(String uuid, String username, boolean type, String token) {
        tokenString = token;
        creationTime = System.currentTimeMillis();
        usernameString = username;
        uuidString = uuid;
        tokenType = type;
    }

    public String token() {return tokenString;}
    public long creation() {return creationTime;}
    public String username() {return usernameString;}
    public String uuid() {return uuidString;}
}
