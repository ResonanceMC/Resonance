package dev.razboy.resonance.listener;

import dev.razboy.resonance.request.SyncReqManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MessageListener implements Listener {
    private final SyncReqManager webSocketManager;

    public MessageListener(SyncReqManager webSocketManager) {
        this.webSocketManager = webSocketManager;
    }

    @EventHandler
    public void onMessage(AsyncChatEvent event) {
        webSocketManager.send(event.message());
    }
}
