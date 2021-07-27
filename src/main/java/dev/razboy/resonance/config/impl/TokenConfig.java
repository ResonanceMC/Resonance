package dev.razboy.resonance.config.impl;

import com.google.common.collect.BiMap;
import dev.razboy.resonance.config.Config;
import dev.razboy.resonance.token.Token;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class TokenConfig extends Config {
    private static final String USERS = "Users.";
    private static final String TOKENS = "Tokens.";
    @Override
    protected String getFileName() {
        return "tokens.yml";
    }

    @Override
    protected void initializeConfig() {
        this.options().header("=== Tokens ~ DO NOT EDIT === #");
        this.options().copyDefaults(true);

        //this.createSection("Tokens");
        //this.createSection("Users");
        this.save();
    }

    public HashMap<Token, String> getTokens() {
        HashMap<Token, String> tokens = new HashMap<>();
        try {
            if (this.getConfigurationSection("Users") == null) {this.createSection("Users");}
            if (this.getConfigurationSection("Tokens") == null) {this.createSection("Tokens");}


            ConfigurationSection userSection = Objects.requireNonNull(this.getConfigurationSection("Users"));
            ConfigurationSection tokenSection = Objects.requireNonNull(this.getConfigurationSection("Tokens"));
            Set<String> keys = tokenSection.getKeys(false);
            keys.forEach((key) -> {
                //System.out.println(key + "/" + Objects.requireNonNull(tokenSection.getString(key)));
                if (userSection.contains(Objects.requireNonNull(tokenSection.getString(key)))) {
                    ConfigurationSection user = Objects.requireNonNull(userSection.getConfigurationSection(Objects.requireNonNull(tokenSection.getString(key))));
                    tokens.put(new Token(
                            user.getString("Token"),
                            user.getLong("Creation"),
                            user.getName(),
                            user.getString("Username"),
                            true
                    ), user.getName());
                } else {
                    tokenSection.set(key, null);
                }
            });
            this.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public void saveTokens(BiMap<Token, String> tokens) {
        try {
            this.set("Tokens", null);
            this.set("Users", null);

            tokens.keySet().forEach((token) -> {
                //System.out.println(token.token() + token.uuid());
                this.set(TOKENS + token.token(), token.uuid());
                this.set(USERS + token.uuid() + ".Token", token.token());
                this.set(USERS + token.uuid() + ".Creation", token.creation());
                this.set(USERS + token.uuid() + ".Username", token.username());
            });

            this.save();
        } catch (Exception e) {e.printStackTrace();}
    }
}
