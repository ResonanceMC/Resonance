package net.thiccaxe.resonance.auth.uuid;

import net.thiccaxe.resonance.auth.TokenStorage;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UuidStorage implements TokenStorage<UUID, UuidToken, String> {


    private final HoconConfigurationLoader loader;

    private UuidStorage.TokenFile tokenFile;
    private CommentedConfigurationNode node;

    public UuidStorage(Path path) throws ConfigurateException {
        loader = HoconConfigurationLoader.builder()
                .defaultOptions(options ->
                        options.shouldCopyDefaults(true)
                )
                .path(path)
                .build();

        node = loader.load();
        tokenFile = node.get(UuidStorage.TokenFile.class);
    }


    @Override
    public void saveTokens(Tokens<UUID, UuidToken, String> tokens) throws ConfigurateException {
        tokenFile.tokens = new ArrayList<>(tokens.keyToTokenMap().values());
        node.set(UuidStorage.TokenFile.class, tokenFile);
        loader.save(node);
    }

    @Override
    public UuidStorage.BasicTokens loadTokens() throws ConfigurateException {

        node = loader.load();
        tokenFile = node.get(UuidStorage.TokenFile.class);

        Map<String, UUID> tokenValueToKeyMap = new ConcurrentHashMap<>();
        Map<UUID, UuidToken> keyToTokenMap = new ConcurrentHashMap<>();

        if (tokenFile == null) {
            tokenFile = new UuidStorage.TokenFile(new ArrayList<>());
        }

        for (UuidToken token : tokenFile.tokens) {
            tokenValueToKeyMap.put(token.getValue(), token.getKey());
            keyToTokenMap.put(token.getKey(), token);
        }
        return new UuidStorage.BasicTokens(keyToTokenMap, tokenValueToKeyMap);
    }

    @Override
    public UuidStorage.BasicTokens reloadTokens(Tokens<UUID, UuidToken, String> tokens) throws ConfigurateException {
        saveTokens(tokens);
        return loadTokens();
    }

    public static final class BasicTokens implements Tokens<UUID, UuidToken, String> {

        private final Map<UUID, UuidToken> keyToTokenMap;
        private final Map<String, UUID> tokenValueToKeyMap;

        public BasicTokens(Map<UUID, UuidToken> keyToTokenMap, Map<String, UUID> tokenValueToKeyMap) {
            this.keyToTokenMap = keyToTokenMap;
            this.tokenValueToKeyMap = tokenValueToKeyMap;
        }

        @Override
        public Map<UUID, UuidToken> keyToTokenMap() {
            return keyToTokenMap;
        }

        @Override
        public Map<String, UUID> tokenValueToKeyMap() {
            return tokenValueToKeyMap;
        }
    }

    @ConfigSerializable
    public static class TokenFile {

        private List<UuidToken> tokens;

        public TokenFile(List<UuidToken> tokens) {
            this.tokens = tokens;
        }

        public TokenFile() {
            this.tokens = new ArrayList<UuidToken>();
        }

    }


}
