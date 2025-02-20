package com.thatoneaiguy.archipelago.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface Brushable {
    boolean brush(long l, World world, PlayerEntity player, Direction direction, BlockPos pos, BlockState state);
}
