package net.thiccaxe.resonance.platform.logger;

import java.io.PrintStream;

public class SystemLogger implements Logger<PrintStream> {

    private final String name;
    private final String format = "[%s] %s%n";


    public SystemLogger(String name) {
        this.name = name;
    }

    @Override
    public void info(String s) {
        System.out.printf(format, name, s);
    }

    @Override
    public void error(String s) {
        System.err.printf(format, name, s);
    }

    @Override
    public void warn(String s) {
        System.out.printf(format, name, s);
    }

    @Override
    public PrintStream logger() {
        return System.out;
    }
}
