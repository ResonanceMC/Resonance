package dev.razboy.resonance.command;

import dev.razboy.resonance.Resonance;
import dev.razboy.resonance.config.ConfigManager;
import dev.razboy.resonance.config.ConfigType;
import dev.razboy.resonance.config.impl.DefaultConfig;
import dev.razboy.resonance.token.Token;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TokenCommand implements CommandExecutor {
    private static final MiniMessage  miniMessage = MiniMessage.get();
    public static final String TokenMessage = "<aqua>A token has been generated!\nToken: <hover:show_text:'<white>Click to copy your token'><click:copy_to_clipboard:%TOKEN%><gold>%TOKEN%</gold></click></hover>\nDirect Link: <hover:show_text:'<white>Click to open the website'><click:open_url:'%URL%'><gold>%URL%</gold></click></hover>";
    public static final String LoggedInMessage = "<aqua>Someone has used the token you just generated!\nIf this was <bold>not</bold> you, <hover:show_text:'<red>Click here to disconnect the other user'><click:run_command:'/help'><bold><gold>CLICK HERE</gold></bold></click></hover>";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {return false;}
        Player player = (Player) sender;
        if (args.length < 1) {
            Token token = Resonance.getTokenManager().generateAuthToken(player);
            parseMessage(player, token);
            return true;
        }
        if (args[0].equalsIgnoreCase("l")) {
            player.sendMessage(Resonance.getTokenManager().listTokens());
            return true;
        } else if (args[0].equalsIgnoreCase("c")) {
            Token token = new Token(player.getUniqueId().toString(), player.getName(), true);
            Resonance.getTokenManager().registerToken(token);
            parseMessage(player, token);
        }
        return false;


    }

    private void parseMessage(Player player, Token token) {
        String tokenMessage = TokenMessage;
        try {
            tokenMessage = tokenMessage.replaceAll("%TOKEN%", token.token());
        } catch (Exception ignored) {}
        try {
            tokenMessage = tokenMessage.replaceAll("%URL%", "https://" + Resonance.getConfigManager().get(ConfigType.DEFAULT).getString("domain", "resonance.razboy.dev") + "/token/" + token.token());
        } catch (Exception ignored) {}
        Component message = Component.newline().append(MiniMessage.get().parse(ConfigManager.getPrefix())).append(MiniMessage.get().parse(tokenMessage)).append(Component.newline());
        player.sendMessage(message);
    }
}
