package dev.razboy.resonance.client;

import dev.razboy.resonance.command.TokenCommand;
import dev.razboy.resonance.config.ConfigManager;
import dev.razboy.resonance.network.Connection;
import dev.razboy.resonance.token.Token;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.UUID;

public class Client {
    private final Connection connection;
    private final Token token;
    private final User user;

    public Client(Connection connection, Token token) {
        this.connection = connection;
        this.token = token;
        this.user = new User(token, Bukkit.getPlayer(UUID.fromString(token.uuid())));
    }

    public Token getToken() {
        return token;
    }
    public Connection getConnection() {
        return connection;
    }
    public User getUser() {
        return user;
    }
    public JSONObject getUserJson() {
        return user.getFullJson();
    }

    public void sendLogInMessage(String ip, String authToken) {
        Player player = user.getPlayer();
        if (player != null){
            String message = TokenCommand.LoggedInMessage;
            try {
                message = message.replace("%IP%", ip);
            } catch (Exception ignored) {}
            try {
                message = message.replace("%TOKEN%", authToken);
            } catch (Exception ignored) {}
            player.sendMessage(Component.newline().append(MiniMessage.get().parse(ConfigManager.getPrefix())).append(MiniMessage.get().parse(message)).append(Component.newline()));
        }
    }
}
