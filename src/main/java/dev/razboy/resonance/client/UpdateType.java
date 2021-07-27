package dev.razboy.resonance.client;

public class UpdateType {
    public static final String ONLINE = "online";
    public static final String POSITION = "position";
    public static final String WORLD = "dimension";

    private String getType(String type) {
        switch (type) {
            case ONLINE:
                return "online";
            case POSITION:
                return "pos";
            case WORLD:
                return "dimension";

        }
        return "null";
    }
}
