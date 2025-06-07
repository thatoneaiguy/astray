package com.thatoneaiguy.archipelago.packet;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import nakern.be_camera.camera.CameraManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class ResetCameraModificationS2C implements S2CPacket {
    public ResetCameraModificationS2C() {}

    @Override
    public void handle(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketSender packetSender, SimpleChannel simpleChannel) {
        minecraftClient.execute(CameraManager.INSTANCE::clear);
    }

    @Override
    public void encode(PacketByteBuf packetByteBuf) {

    }
}
