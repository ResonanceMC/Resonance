package net.thiccaxe.resonance.platform.logger;

public interface Logger<L> {

    void info(String s);

    void error(String s);

    void warn(String s);

    L logger();
}
