package com.thatoneaiguy.archipelago.util.runic;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public interface Spell {
    void cast(ServerWorld world, ServerPlayerEntity user);
}
