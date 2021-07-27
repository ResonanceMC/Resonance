package dev.razboy.resonance.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ParseCommand implements CommandExecutor {
    private static final MiniMessage miniMessage = MiniMessage.get();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {sender.sendMessage(miniMessage.parse(String.join(" ", args))); return true;}
        catch (Exception ignored) {return false;}
    }
}
