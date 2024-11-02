package com.thatoneaiguy.archipelago.block;

import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.awt.*;

public class PrivacyGlassBlock extends GlassBlock implements PrivacyBlock {

	public PrivacyGlassBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState((BlockState) ((BlockState) super.getDefaultState().with(INTERACTION_COOLDOWN, false).with(OPAQUE, true).with(OPEN_CLOSED, false).with(CLOSED_OPEN, false)));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.shouldCancelInteraction() && !player.getStackInHand(hand).isOf(this.asItem()) && this.canInteract(state, pos, world, player, hand)) {
				this.toggle(state, world, pos);
				return ActionResult.success(world.isClient);
			}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		boolean openClosed = (Boolean)state.get(OPEN_CLOSED);
		boolean closedOpen = (Boolean)state.get(CLOSED_OPEN);

		if ( !openClosed && !closedOpen ) {
			this.toggle(state, world, pos);
		} else {
			super.scheduledTick(state, world, pos, random);
		}
	}


	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{OPAQUE, INTERACTION_COOLDOWN, OPEN_CLOSED, CLOSED_OPEN});
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		return world.getMaxLightLevel();
	}

	/*@Override
	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		return world.getMaxLightLevel();
	}*/

}
