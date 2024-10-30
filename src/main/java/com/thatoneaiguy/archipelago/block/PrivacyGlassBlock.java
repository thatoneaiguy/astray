package com.thatoneaiguy.archipelago.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
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
	public boolean locked = false;

	public PrivacyGlassBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState((BlockState) ((BlockState) super.getDefaultState().with(OPAQUE, false)).with(INTERACTION_COOLDOWN, false).with(OPAQUE, true));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean hasChanneling = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, player.getStackInHand(hand)) > 0;
		Text returnMessage;

		if ( locked ) {
			if ( hasChanneling ) {
				returnMessage = Text.literal("Unlocked Glass!");

				returnMessage.copy().setStyle(returnMessage.getStyle().withColor(new Color(135, 255, 191, 255).getRGB()));

				locked = false;
				return super.onUse(state, world, pos, player, hand, hit);
			}
		}

		if ( !locked ) {
			if ( hasChanneling ) {
				returnMessage = Text.literal("Locked Glass!");

				returnMessage.copy().setStyle(returnMessage.getStyle().withColor(new Color(143, 61, 66, 255).getRGB()));

				locked = true;
				return super.onUse(state, world, pos, player, hand, hit);
			}

			if (!player.shouldCancelInteraction() && !player.getStackInHand(hand).isOf(this.asItem()) && this.canInteract(state, pos, world, player, hand)) {
				this.toggle(state, world, pos);
				return ActionResult.success(world.isClient);
			} else {
				return super.onUse(state, world, pos, player, hand, hit);
			}

		} else {
			player.sendMessage((Text) Text.literal("This glass is locked!").getStyle().withColor(new Color(143, 61, 66, 255).getRGB()));
			world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundCategory.BLOCKS, 1.0f, 0.8f);

			return super.onUse(state, world, pos, player, hand, hit);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.toggle(state, world, pos);
	}



	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{OPAQUE, INTERACTION_COOLDOWN});
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	/*@Override
	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		return world.getMaxLightLevel();
	}*/

}
