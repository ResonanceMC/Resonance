package net.thiccaxe.resonance.network.javalin;

import io.javalin.Javalin;
import net.thiccaxe.resonance.Resonance;
import org.jetbrains.annotations.NotNull;

public class JavalinServer {
    private final @NotNull Resonance resonance;
    private final @NotNull Javalin app;
    private ClassLoader _class_loader;

    public JavalinServer(@NotNull Resonance resonance) {
        this.resonance = resonance;

        // https://gist.github.com/RezzedUp/d7957af10bfbfc6837ae1a4b55975f40
        startPluginClassLoader(resonance.platform().useJavalinClassLoaderHack());
        app = Javalin.create(cfg -> cfg.showJavalinBanner = false);
        app.get("/", ctx -> ctx.result("Welcome to Resonance!"));
        app.ws("/test", ws -> {
            ws.onMessage(ctx->{
                ctx.send(ctx.message());
            });
        });
        /*
        app.ws("/ws/:ticket", ws -> {
            ws.onConnect(ctx -> {
                resonance.platform().logger().info(ctx.pathParam("ticket"));
                ctx.send("test");
            });
            ws.onMessage(ctx -> {
                ctx.send(ctx.message());
            });
        });
         */
        endPluginClassLoader(resonance.platform().useJavalinClassLoaderHack());
    }

    public void start(int port) {
        startPluginClassLoader(resonance.platform().useJavalinClassLoaderHack());
        this.app.start(port);
        endPluginClassLoader(resonance.platform().useJavalinClassLoaderHack());
    }

    public void stop() {
        this.app.stop();
    }


    private void startPluginClassLoader(boolean useLoader){
        if (useLoader) {
            _class_loader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(resonance.platform().classLoaderForJavalinHack());
        }
    }

    private void endPluginClassLoader(boolean useLoader){
        if (useLoader && _class_loader != null) {
            Thread.currentThread().setContextClassLoader(_class_loader);
        }
    }
}
