package net.thiccaxe.resonance.auth.basic;

import io.leangen.geantyref.TypeToken;
import net.thiccaxe.resonance.auth.TokenStorage;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.NodeResolver;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.util.NamingScheme;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicStorage implements TokenStorage<String, StringToken, String> {


    private final HoconConfigurationLoader loader;

    private TokenFile tokenFile;
    private CommentedConfigurationNode node;

    public BasicStorage(Path path) throws ConfigurateException {
        loader = HoconConfigurationLoader.builder()
                .defaultOptions(options ->
                    options.shouldCopyDefaults(true)
                )
                .path(path)
                .build();

        node = loader.load();
        tokenFile = node.get(TokenFile.class);
    }


    @Override
    public void saveTokens(Tokens<String, StringToken, String> tokens) throws ConfigurateException {
        tokenFile.tokens = new ArrayList<>(tokens.keyToTokenMap().values());
        node.set(TokenFile.class, tokenFile);
        loader.save(node);
    }

    @Override
    public BasicTokens loadTokens() throws ConfigurateException {

        node = loader.load();
        tokenFile = node.get(TokenFile.class);

        Map<String, String> tokenValueToKeyMap = new HashMap<>();
        Map<String, StringToken> keyToTokenMap = new HashMap<>();

        if (tokenFile == null) {
            tokenFile = new TokenFile(new ArrayList<>());
        }

        for (StringToken token : tokenFile.tokens) {
            tokenValueToKeyMap.put(token.getValue(), token.getKey());
            keyToTokenMap.put(token.getKey(), token);
        }
        return new BasicTokens(keyToTokenMap, tokenValueToKeyMap);
    }

    @Override
    public BasicTokens reloadTokens(Tokens<String, StringToken, String> tokens) throws ConfigurateException {
        saveTokens(tokens);
        return loadTokens();
    }

    public static final class BasicTokens implements Tokens<String, StringToken, String> {

        private final Map<String, StringToken> keyToTokenMap;
        private final Map<String, String> tokenValueToKeyMap;

        public BasicTokens(Map<String, StringToken> keyToTokenMap, Map<String, String> tokenValueToKeyMap) {
            this.keyToTokenMap = keyToTokenMap;
            this.tokenValueToKeyMap = tokenValueToKeyMap;
        }

        @Override
        public Map<String, StringToken> keyToTokenMap() {
            return keyToTokenMap;
        }

        @Override
        public Map<String, String> tokenValueToKeyMap() {
            return tokenValueToKeyMap;
        }
    }

    @ConfigSerializable
    public static class TokenFile {

        private List<StringToken> tokens;

        public TokenFile(List<StringToken> tokens) {
            this.tokens = tokens;
        }

        public TokenFile() {
            this.tokens = new ArrayList<>();
        }

    }


}
