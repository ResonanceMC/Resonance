package net.thiccaxe.resonance.platform.logger;

public class Slf4jLogger implements Logger<org.slf4j.Logger> {

    private final org.slf4j.Logger logger;

    public Slf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        logger.info(s);
    }

    @Override
    public void error(String s) {
        logger.error(s);
    }

    @Override
    public void warn(String s) {
        logger.warn(s);
    }

    @Override
    public org.slf4j.Logger logger() {
        return logger;
    }
}
