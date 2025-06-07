package com.thatoneaiguy.archipelago.util;

public class HotbarRenderingUtil {
    private static boolean isHotbarRendered = true;

    public static boolean isHotbarRendered() {
        return isHotbarRendered;
    }

    public static void setHotbarRendered(boolean hotbarRendered) {
        isHotbarRendered = hotbarRendered;
    }
}
