package dev.razboy.resonance.request;

import dev.razboy.resonance.Resonance;
import dev.razboy.resonance.client.Client;
import dev.razboy.resonance.client.Clients;
import dev.razboy.resonance.network.Request;
import dev.razboy.resonance.packets.Packet;
import dev.razboy.resonance.packets.clientbound.auth.AuthFailedPacket;
import dev.razboy.resonance.packets.clientbound.auth.AuthenticatedPacket;
import dev.razboy.resonance.packets.clientbound.play.OUserInfoPacket;
import dev.razboy.resonance.packets.clientbound.play.PeerConnectPacket;
import dev.razboy.resonance.packets.clientbound.play.PeerDisconnectPacket;
import dev.razboy.resonance.packets.serverbound.ServerBoundPacket;
import dev.razboy.resonance.packets.serverbound.auth.AuthTokenAuthenticatePacket;
import dev.razboy.resonance.packets.serverbound.auth.LogoutPacket;
import dev.razboy.resonance.packets.serverbound.play.PeerInfoPacket;
import dev.razboy.resonance.packets.serverbound.play.PeerRelayIceCandidatePacket;
import dev.razboy.resonance.packets.serverbound.play.UserConnectPacket;
import dev.razboy.resonance.packets.serverbound.play.UserDisconnectPacket;
import dev.razboy.resonance.packets.serverbound.auth.UserInfoPacket;
import dev.razboy.resonance.token.Token;
import dev.razboy.resonance.token.TokenManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.kyori.adventure.text.Component;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SyncReqManager extends IRequestManager {
    private final Resonance plugin;
    private static final List<String> ACTIONS = Arrays.asList("authenticate", "logout", "user_info");
    private final Clients clients = new Clients();
    private final ConcurrentLinkedQueue<Component> sendQueue = new ConcurrentLinkedQueue<>();
    public SyncReqManager(Resonance instance) {
        plugin = instance;
    }

    private void close(Request request) {
        close(request.ctx);
    }
    private void close(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new CloseWebSocketFrame());
        ctx.close();
    }


    @Override
    protected void additional() {
        for (Component message : sendQueue) {
            if (message != null) {
                clients.sendAll(message);
            }
        }
        sendQueue.clear();
        clients.update();
    }

    @Override
    public void handleIncoming(Request request) {
        if (request.webSocketFrame == null) {
            return;
        }
        try {
            Packet packet = request.packet;
            if (packet instanceof ServerBoundPacket) {
                //System.out.println(packet.repr());
                if (packet instanceof AuthTokenAuthenticatePacket) {
                    authTokenAuthenticate(request, packet);
                } else if (packet instanceof LogoutPacket) {
                    logoutUser(request, packet);
                } else if (packet instanceof UserConnectPacket) {
                    //System.out.println(packet.repr());
                    broadcastConnect(request, packet);
                } else if (packet instanceof UserDisconnectPacket) {
                    //System.out.println(packet.repr());
                    broadcastDisconnect(request, packet);
                    disconnectUser(request, packet);
                } else if (packet instanceof UserInfoPacket) {
                    tokenAuthenticate(request, packet);
                } else if (packet instanceof PeerInfoPacket) {
                    sendPeers(request, packet);
                } else if (packet instanceof PeerRelayIceCandidatePacket) {
                    handleIceRelay(request, packet);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIceRelay(Request request, Packet p) {
        Client client = clients.getClient(request.connection);
        if (client != null) {
            PeerRelayIceCandidatePacket packet = (PeerRelayIceCandidatePacket) p;
            clients.relayIce(client, packet);
        }
    }

    private void sendPeers(Request request, Packet packet) {
        Client client = clients.getClient(request.connection);
        if (client != null) {
            clients.sendPeerInfo(client, packet);
        }
    }

    private void broadcastDisconnect(Request request, Packet packet) {
        Client client = clients.getClient(request.connection);
        if (client != null) {
            JSONObject data = client.getUser().withData();
            PeerDisconnectPacket peerDisconnectPacket = new PeerDisconnectPacket();
            peerDisconnectPacket.setUser(data);
            clients.sendAllBut(request.setPacket(peerDisconnectPacket));
        }
    }

    private void broadcastConnect(Request request, Packet packet) {
         Client client = clients.getClient(request.connection);
         if (client != null) {
             JSONObject data = client.getUserJson();
             //.out.println(data.toString());
             PeerConnectPacket peerConnectPacket = new PeerConnectPacket();
             peerConnectPacket.setUser(data);
             clients.sendAllBut(request.setPacket(peerConnectPacket));
         }
    }

    private void disconnectUser(Request request, Packet packet) {
        if (clients.hasClient(request.connection)) {
            clients.removeClient(request.connection);
        }
    }

    @Override
    protected void handleOutgoing(Request request) {

    }

    private void logoutUser(Request request, Packet packet) {
        disconnectUser(request, packet);
        clients.logoutClient(request.connection);
    }

    private void tokenAuthenticate(Request request, Packet p) {
        UserInfoPacket packet = (UserInfoPacket) p;

        String bearer = packet.getToken();
        TokenManager tokenManager = Resonance.getTokenManager();
        Token token = tokenManager.validateToken(bearer);
        if (token != null) {
            Client client = clients.addClient(token, request.connection);
            OUserInfoPacket userInfoPacket = new OUserInfoPacket();
            userInfoPacket.setMessageId(packet.getMessageId());
            userInfoPacket.setToken(token.token());
            userInfoPacket.setUser(client.getUserJson());
            //System.out.println(userInfoPacket.repr());
            send(request.setPacket(userInfoPacket));
            clients.sendPeerInitial(client);
            return;
        }

        send(request.setPacket(new AuthFailedPacket().setMessageId(packet.getMessageId())));
    }


    private void authTokenAuthenticate(Request request, Packet p) {
        AuthTokenAuthenticatePacket packet = (AuthTokenAuthenticatePacket) p;
            if (packet.getAuthToken() != null) {
                Token token = Resonance.getTokenManager().validateAuthToken(packet.getAuthToken());
                if (token != null) {
                    Client client = clients.addClient(token, request.connection);

                    AuthenticatedPacket authenticatedPacket = new AuthenticatedPacket();
                    authenticatedPacket.setMessageId(packet.getMessageId());
                    authenticatedPacket.setToken(client.getToken().token());
                    JSONObject user = client.getUserJson();
                    authenticatedPacket.setUser(client.getUserJson());
                    //System.out.println(request.setPacket(authenticatedPacket).packet.repr());
                    send(request.setPacket(authenticatedPacket));
                    clients.getClient(token.token()).sendLogInMessage(request.connection.getRemote(), packet.getAuthToken());
                    clients.sendPeerInitial(client);
                    return;
                }
            }
        send(request.setPacket(new AuthFailedPacket().setMessageId(packet.getMessageId())));

    }

    public void send(Component message) {
        sendQueue.add(message);
    }
    public static void send(Request request) {
        request.ctx.writeAndFlush(new TextWebSocketFrame(request.packet.read()));
    }

}
