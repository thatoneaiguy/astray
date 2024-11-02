package com.thatoneaiguy.archipelago.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PrivacyGlassPanelBlock extends GlassPanelBlock implements PrivacyBlock {
	public PrivacyGlassPanelBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState((BlockState)((BlockState)super.getDefaultState().with(OPAQUE, false)).with(INTERACTION_COOLDOWN, false));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.shouldCancelInteraction() && !player.getStackInHand(hand).isOf(this.asItem()) && this.canInteract(state, pos, world, player, hand)) {
			this.toggle(state, world, pos);
			return ActionResult.success(world.isClient);
		} else {
			return super.onUse(state, world, pos, player, hand, hit);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{OPAQUE, INTERACTION_COOLDOWN, CLOSED_OPEN, OPEN_CLOSED});
		super.appendProperties(builder);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		return world.getMaxLightLevel();
	}
}

