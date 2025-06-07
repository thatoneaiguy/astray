package com.thatoneaiguy.archipelago.init;

import com.thatoneaiguy.archipelago.Archipelago;
import com.thatoneaiguy.archipelago.packet.CameraModificationS2C;
import com.thatoneaiguy.archipelago.packet.ResetCameraModificationS2C;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.util.Identifier;

public class ArchipelagoPackets {
    public static final SimpleChannel CHANNEL = new SimpleChannel(new Identifier(Archipelago.MODID, "main"));

    public static void registerS2C() {
        CHANNEL.registerS2CPacket(CameraModificationS2C.class, 1);
        CHANNEL.registerS2CPacket(ResetCameraModificationS2C.class, 2);
    }

    public static void registerC2S() {}
}
