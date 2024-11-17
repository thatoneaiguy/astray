package com.thatoneaiguy.archipelago.block;

import com.thatoneaiguy.archipelago.Archipelago;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SpyglassItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Set;

public interface PrivacyBlock {
	// transition
	BooleanProperty OPEN_CLOSED = BooleanProperty.of("open_closed");
	BooleanProperty CLOSED_OPEN = BooleanProperty.of("closed_open");
	// states
	BooleanProperty OPAQUE = BooleanProperty.of("opaque");
	BooleanProperty INTERACTION_COOLDOWN = BooleanProperty.of("interaction_cooldown");
	Direction[][] DIAGONALS = new Direction[][]{{Direction.NORTH, Direction.EAST}, {Direction.SOUTH, Direction.EAST}, {Direction.SOUTH, Direction.WEST}, {Direction.NORTH, Direction.WEST}, {Direction.UP, Direction.NORTH}, {Direction.UP, Direction.EAST}, {Direction.UP, Direction.SOUTH}, {Direction.UP, Direction.WEST}, {Direction.DOWN, Direction.NORTH}, {Direction.DOWN, Direction.EAST}, {Direction.DOWN, Direction.SOUTH}, {Direction.DOWN, Direction.WEST}};

	int COOLDOWN = 5;


	default void toggle(BlockState state, World world, BlockPos pos) {
		boolean opaque = !(Boolean)state.get(OPAQUE);

		if ((Boolean)state.get(INTERACTION_COOLDOWN)) {
			world.setBlockState(pos, (BlockState)state.with(INTERACTION_COOLDOWN, false));
		} else {
			world.playSound((PlayerEntity) null, pos, Archipelago.PRIVACY_GLASS_TOGGLE_EVENT, SoundCategory.BLOCKS, 0.5F, opaque ? 1.0F : 1.2F);
			world.setBlockState(pos, (BlockState)((BlockState)state.with(OPAQUE, opaque)).with(INTERACTION_COOLDOWN, true).with(OPEN_CLOSED, false).with(CLOSED_OPEN, false));
			world.createAndScheduleBlockTick(pos, state.getBlock(), COOLDOWN);
			Set<Direction> changedDirections = EnumSet.noneOf(Direction.class);
			Direction[] var6 = Direction.values();
			int var7 = var6.length;

			int var8;
			BlockPos diagonalPos;
			BlockState diagonalState;
			for(var8 = 0; var8 < var7; ++var8) {
				Direction direction = var6[var8];
				diagonalPos = pos.offset(direction);
				diagonalState = world.getBlockState(diagonalPos);
				if (this.canToggle(diagonalState) && (Boolean)diagonalState.get(OPAQUE) != opaque) {
					changedDirections.add(direction);
					world.createAndScheduleBlockTick(diagonalPos, diagonalState.getBlock(), 1);
				}
			}

			Direction[][] var12 = DIAGONALS;
			var7 = var12.length;

			for(var8 = 0; var8 < var7; ++var8) {
				Direction[] diagonal = var12[var8];
				if (!this.diagonalHasAdjacentBlock(diagonal, changedDirections)) {
					diagonalPos = this.offsetDiagonal(pos, diagonal);
					diagonalState = world.getBlockState(diagonalPos);
					if (this.canToggle(diagonalState) && (Boolean)diagonalState.get(OPAQUE) != opaque) {
						world.createAndScheduleBlockTick(diagonalPos, diagonalState.getBlock(), 1);
					}
				}
			}

		}
	}

	default boolean diagonalHasAdjacentBlock(Direction[] diagonal, Set<Direction> changedDirections) {
		return changedDirections.contains(diagonal[0]) || changedDirections.contains(diagonal[1]);
	}

	default BlockPos offsetDiagonal(BlockPos pos, Direction[] diagonal) {
		return pos.offset(diagonal[0]).offset(diagonal[1]);
	}

	default boolean canInteract(BlockState state, BlockPos pos, World world, PlayerEntity player, Hand hand) {
		if ((Boolean)state.get(INTERACTION_COOLDOWN)) {
			return false;
		} else if (player.getStackInHand(hand).getItem() instanceof SpyglassItem) {
			return false;
		} else {
			Direction[] var6 = Direction.values();
			int var7 = var6.length;

			int var8;
			for(var8 = 0; var8 < var7; ++var8) {
				Direction direction = var6[var8];
				BlockState sideState = world.getBlockState(pos.offset(direction));
				if (sideState.contains(INTERACTION_COOLDOWN) && (Boolean)sideState.get(INTERACTION_COOLDOWN)) {
					return false;
				}
			}

			Direction[][] var12 = DIAGONALS;
			var7 = var12.length;

			for(var8 = 0; var8 < var7; ++var8) {
				Direction[] diagonal = var12[var8];
				BlockPos diagonalPos = this.offsetDiagonal(pos, diagonal);
				BlockState diagonalState = world.getBlockState(diagonalPos);
				if (diagonalState.contains(INTERACTION_COOLDOWN) && (Boolean)diagonalState.get(INTERACTION_COOLDOWN)) {
					return false;
				}
			}

			return true;
		}
	}

	default boolean canToggle(BlockState state) {
		return state.getBlock() instanceof PrivacyBlock;
	}
}
