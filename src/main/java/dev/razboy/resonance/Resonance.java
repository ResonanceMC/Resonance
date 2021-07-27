package dev.razboy.resonance;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.razboy.resonance.command.ParseCommand;
import dev.razboy.resonance.command.TokenCommand;
import dev.razboy.resonance.config.ConfigManager;
import dev.razboy.resonance.config.ConfigType;
import dev.razboy.resonance.config.impl.TokenConfig;
import dev.razboy.resonance.listener.MessageListener;
import dev.razboy.resonance.network.Server;
import dev.razboy.resonance.request.*;
import dev.razboy.resonance.token.TokenManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

public class Resonance extends JavaPlugin {
    private static Resonance instance;
    private static File dataFolder;
    private static File tokenFile;

    private static final Properties properties = new Properties();
    private static final BiMap<String, String> something = HashBiMap.create();

    private static final Server server = new Server();
    private static AsyncReqManager httpRequestManager;
    private static SyncReqManager webSocketRequestManager;


    private static ConfigManager configManager;
    private static TokenManager tokenManager;

    private BukkitTask serverTask;
    private BukkitTask httpTask;
    private BukkitTask wsTask;

    public static TokenManager getTokenManager() {return tokenManager;}


    @Override public void onEnable() {
        System.out.println("Starting Server...");
        instance = this;
        dataFolder = getDataFolder();
        configManager = new ConfigManager(dataFolder);
        tokenManager = new TokenManager(this, (TokenConfig)configManager.get(ConfigType.TOKENS));
        //tokenManager.generateAuthToken(UUID.fromString("bee4331f-7bf2-4d92-b8c4-82cd2f5f38cc").toString(), "4O3F0rbidden", "ABCDEF");




        webSocketRequestManager = new SyncReqManager(this);
        httpRequestManager = new AsyncReqManager(this);
        wsTask = Bukkit.getScheduler().runTaskTimer (this, webSocketRequestManager, 10, 1);
        httpTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, httpRequestManager, 10, 1);
        serverTask = Bukkit.getScheduler().runTaskAsynchronously(this, server);
        getServer().getPluginManager().registerEvents(new MessageListener(webSocketRequestManager), this);
        Objects.requireNonNull(this.getCommand("token")).setExecutor(new TokenCommand());
        Objects.requireNonNull(this.getCommand("minimessage")).setExecutor(new ParseCommand());
    }

    @Override public void onDisable() {
        server.stop();
        serverTask.cancel();
        webSocketRequestManager.stop();
        wsTask.cancel();
        httpRequestManager.stop();
        httpTask.cancel();
    }




    public static AsyncReqManager getHttpRequestManager() {
        return httpRequestManager;
    }

    public static SyncReqManager getWebSocketRequestManager() {return webSocketRequestManager;}
    public static void log(String message) {
        instance.getLogger().info(message);
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
