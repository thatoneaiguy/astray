package com.thatoneaiguy.archipelago.block;

import com.thatoneaiguy.archipelago.init.ArchipelagoItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends Block {
    public static final IntProperty CHARGE = IntProperty.of("charge", 0, 3);

    public JarBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(CHARGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGE);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        ItemStack stack = ctx.getStack();
        int charge = stack.getOrCreateNbt().getInt("charge");
        return getDefaultState().with(CHARGE, charge);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            ItemStack stack = new ItemStack(ArchipelagoItems.JAR);
            stack.getOrCreateNbt().putInt("charge", state.get(CHARGE)); // Store charge in item
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }

        super.onBreak(world, pos, state, player);
    }
}
