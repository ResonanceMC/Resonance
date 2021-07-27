package dev.razboy.resonance.client;

import com.google.common.collect.HashBiMap;
import dev.razboy.resonance.Resonance;
import dev.razboy.resonance.network.Connection;
import dev.razboy.resonance.network.Request;
import dev.razboy.resonance.packets.Packet;
import dev.razboy.resonance.packets.clientbound.play.OPeerInfoPacket;
import dev.razboy.resonance.packets.clientbound.play.OPeerRelayIceCandidatePacket;
import dev.razboy.resonance.packets.clientbound.play.PeerUpdatePacket;
import dev.razboy.resonance.packets.clientbound.play.UserUpdatePacket;
import dev.razboy.resonance.packets.serverbound.play.PeerRelayIceCandidatePacket;
import dev.razboy.resonance.request.SyncReqManager;
import dev.razboy.resonance.token.Token;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.kyori.adventure.text.Component;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Clients {
    private final HashBiMap<Connection, String> connections = HashBiMap.create();
    private final HashBiMap<String, Client> clients = HashBiMap.create();
    private final HashBiMap<String, Client> clientUuids = HashBiMap.create();

    public Clients(){}

    public Client addClient(Token token, Connection connection) {
        connections.forcePut(connection, token.token());
        Client client = new Client(connection, token);
        clients.forcePut(token.token(), client);
        clientUuids.forcePut(token.uuid(), client);
        return client;
    }
    public boolean hasClient(String token) {
        return connections.containsValue(token);
    }
    public boolean hasClient(Connection connection) {
        return connections.containsKey(connection);
    }

    public Client getClient(String token) {
        if (connections.containsValue(token)) {
            return clients.get(token);
        }
        return null;
    }
    public Client getClient(Connection connection) {
        if (connections.containsKey(connection)) {
            if (clients.containsKey(connections.get(connection))) {
                return clients.get(connections.get(connection));
            }
        }
        return null;
    }
    public Client getClient(UUID uuid) {
        if (clientUuids.containsKey(uuid.toString())) {
            return clientUuids.get(uuid.toString());
        }
        return null;
    }


    public void removeClient(Connection connection) {
        if (connections.containsKey(connection)) {
            String token = connections.get(connection);
            if (clients.containsKey(token)) {
                Client c = Objects.requireNonNull(clients.get(token));
                clientUuids.remove(c.getToken().uuid());
            }
        }

        connections.remove(connection);
        clients.remove(connections.get(connection));

    }

    public void sendAll(Component message) {
        /*
        String json = new JSONObject().put("action", "message").put("message", message).toString();
        connections.forEach(
                (connection, token) -> {
                    connection.getCtx().writeAndFlush(new TextWebSocketFrame(json));
                });
         **/
    }
    public void update() {
        HashMap<Client, JSONObject> clientUpdates = new HashMap<>();
        clients.forEach((token, client) -> {
            if (client.getUser().onlineUpdate()) {
                UserUpdatePacket userUpdatePacket = new UserUpdatePacket();
                JSONObject updatedOnline = client.getUser().updateOnline();
                clientUpdates.put(client, updatedOnline.put("type", UpdateType.ONLINE));
                userUpdatePacket.setUser(updatedOnline);
                userUpdatePacket.setType(UserUpdatePacket.ONLINE);
                Object data = updatedOnline.remove("data");
                SyncReqManager.send(new Request(client.getConnection(), userUpdatePacket));
                updatedOnline.put("data", data);
            }
            if (client.getUser().isOnline()) {
                if (client.getUser().positionUpdate()) {
                    UserUpdatePacket userUpdatePacket = new UserUpdatePacket();
                    JSONObject updatedPosition = client.getUser().updatePosition();
                    clientUpdates.put(client, updatedPosition.put("type", UpdateType.POSITION));
                    userUpdatePacket.setUser(updatedPosition);
                    userUpdatePacket.setType(UserUpdatePacket.POSITION);
                    Object data = updatedPosition.remove("data");
                    SyncReqManager.send(new Request(client.getConnection(), userUpdatePacket));
                    updatedPosition.put("data", data);
                }
                if (client.getUser().worldUpdate()) {
                    UserUpdatePacket userUpdatePacket = new UserUpdatePacket();
                    JSONObject updatedWorld = client.getUser().updateWorld();
                    clientUpdates.put(client, updatedWorld.put("type", UpdateType.WORLD));
                    userUpdatePacket.setUser(updatedWorld);
                    userUpdatePacket.setType(UserUpdatePacket.WORLD);
                    Object data = updatedWorld.remove("data");
                    SyncReqManager.send(new Request(client.getConnection(), userUpdatePacket));
                    updatedWorld.put("data", data);
                }
            }
        });
        if (clients.size() > 1) {
            clients.forEach((token, client) -> {
                PeerUpdatePacket peerUpdatePacket = new PeerUpdatePacket();
                clientUpdates.forEach((peer, json) -> {
                    if (!peer.getToken().uuid().equals(client.getToken().uuid())) {
                        peerUpdatePacket.addPeer(json);
                    }
                });
                if (peerUpdatePacket.needsSending()) {
                    SyncReqManager.send(new Request(client.getConnection(), peerUpdatePacket));
                }
            });
        }
        /*
        clientInfo.keySet().forEach((client -> {
            clientInfo.keySet().forEach((peer -> {
                PeerUpdatePacket packet = new PeerUpdatePacket();
                if (peer != client) {
                    packet.addPeer(clientInfo.get(peer));
                }
                peer.getConnection().getCtx().writeAndFlush(new TextWebSocketFrame(packet.read()));
            }));
        }));
        */
    }

    public void logoutClient(Connection connection) {
        if (connections.containsKey(connection)) {
            if (clients.containsKey(connections.get(connection))) {
                Token token = Objects.requireNonNull(clients.get(connections.get(connection))).getToken();
                Resonance.getTokenManager().invalidateToken(token);
            }
        }
        removeClient(connection);
    }

    public void sendAllBut(Request request) {
        //System.out.println(request.packet.repr());
        String message = request.packet.read();
        clients.forEach((token, client) -> {
            if (client.getConnection() != request.connection) {
                client.getConnection().getCtx().writeAndFlush(new TextWebSocketFrame(message));
            }
        });
    }

    public void sendPeerInitial(Client client) {
        if (clients.size() > 1) {
            PeerUpdatePacket peerUpdatePacket = new PeerUpdatePacket();
            clients.forEach((token, peer) -> {
                if (peer.getConnection() != client.getConnection()) {
                    peerUpdatePacket.addPeer(peer.getUserJson());
                }
            });
            if (peerUpdatePacket.needsSending()) {
                SyncReqManager.send(new Request(client.getConnection(), peerUpdatePacket));
            }
        }
    }
    public void sendPeerInfo(Client client, Packet packet) {
        OPeerInfoPacket peerInfoPacket = new OPeerInfoPacket();
        peerInfoPacket.setMessageId(packet.getMessageId());
        clients.forEach((t, peer) -> {
            if (peer.getConnection() != client.getConnection()) {
                peerInfoPacket.addPeer(peer.getUserJson());
            }
        });
        //System.out.println(peerInfoPacket.repr());
        SyncReqManager.send(new Request(client.getConnection(), peerInfoPacket));
    }

    public void relayIce(Client client, PeerRelayIceCandidatePacket packet) {
        UUID peerId = UUID.fromString(packet.getPeerId());
        Client c = getClient(peerId);
        if (client != null) {
            OPeerRelayIceCandidatePacket relayPacket = new OPeerRelayIceCandidatePacket();
            relayPacket.setPeerId(client.getToken().uuid());
            relayPacket.setIceCandidate(packet.getIceCandidate());
            SyncReqManager.send(new Request(c.getConnection(), relayPacket));
        }
    }
}
