package net.thiccaxe.resonance.platform.logger;

public class JavaLogger implements Logger<java.util.logging.Logger> {

    private final java.util.logging.Logger logger;

    public JavaLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        logger.info(s);
    }

    @Override
    public void error(String s) {
        logger.severe(s);
    }

    @Override
    public void warn(String s) {
        logger.warning(s);
    }

    @Override
    public java.util.logging.Logger logger() {
        return logger;
    }
}
