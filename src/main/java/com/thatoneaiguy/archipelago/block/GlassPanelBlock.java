package com.thatoneaiguy.archipelago.block;

import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import oshi.jna.platform.mac.SystemB;

public class GlassPanelBlock extends FacingBlock implements Waterloggable, PrivacyBlock {
	public static final BooleanProperty WATERLOGGED;
	public static final VoxelShape NORTH_SHAPE;
	public static final VoxelShape NORTH_COLLISION_SHAPE;
	public static final VoxelShape EAST_SHAPE;
	public static final VoxelShape EAST_COLLISION_SHAPE;
	public static final VoxelShape SOUTH_SHAPE;
	public static final VoxelShape SOUTH_COLLISION_SHAPE;
	public static final VoxelShape WEST_SHAPE;
	public static final VoxelShape WEST_COLLISION_SHAPE;
	public static final VoxelShape UP_SHAPE;
	public static final VoxelShape UP_COLLISION_SHAPE;
	public static final VoxelShape DOWN_SHAPE;
	public static final VoxelShape DOWN_COLLISION_SHAPE;

	public GlassPanelBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState((BlockState)((BlockState)super.getDefaultState().with(WATERLOGGED, false)).with(FACING, Direction.SOUTH).with(OPAQUE, false).with(CLOSED_OPEN, false).with(OPEN_CLOSED, false));
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		boolean openClosed = (Boolean)state.get(OPEN_CLOSED);
		boolean closedOpen = (Boolean)state.get(CLOSED_OPEN);

		if ( !openClosed && !closedOpen ) {
			this.toggle(state, world, pos);
		}
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}



	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context.isHolding(this.asItem())) {
			VoxelShape var10000;
			switch ((Direction)state.get(FACING)) {
				case NORTH:
					var10000 = NORTH_SHAPE;
					break;
				case EAST:
					var10000 = EAST_SHAPE;
					break;
				case SOUTH:
					var10000 = SOUTH_SHAPE;
					break;
				case WEST:
					var10000 = WEST_SHAPE;
					break;
				case UP:
					var10000 = UP_SHAPE;
					break;
				case DOWN:
					var10000 = DOWN_SHAPE;
					break;
				default:
					throw new IncompatibleClassChangeError();
			}

			return var10000;
		} else {
			return this.getCollisionShape(state, world, pos, context);
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape var10000;
		switch ((Direction)state.get(FACING)) {
			case NORTH:
				var10000 = NORTH_COLLISION_SHAPE;
				break;
			case EAST:
				var10000 = EAST_COLLISION_SHAPE;
				break;
			case SOUTH:
				var10000 = SOUTH_COLLISION_SHAPE;
				break;
			case WEST:
				var10000 = WEST_COLLISION_SHAPE;
				break;
			case UP:
				var10000 = UP_COLLISION_SHAPE;
				break;
			case DOWN:
				var10000 = DOWN_COLLISION_SHAPE;
				break;
			default:
				throw new IncompatibleClassChangeError();
		}

		return var10000;
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if ((Boolean)state.get(WATERLOGGED)) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldAccess world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		Direction facing = ctx.getSide();
		BlockState neighborState = world.getBlockState(pos.offset(facing.getOpposite()));
		if (!ctx.shouldCancelInteraction() && neighborState.isOf(this)) {
			Direction neighborFacing = (Direction)neighborState.get(FACING);
			if (!neighborFacing.getAxis().equals(facing.getAxis())) {
				facing = neighborFacing;
			}
		}

		return (BlockState)((BlockState)this.getDefaultState().with(FACING, facing)).with(WATERLOGGED, world.getFluidState(pos).getFluid().equals(Fluids.WATER));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{FACING, WATERLOGGED});
	}

	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		Direction facing = (Direction)state.get(FACING);
		if (stateFrom.isOf(this)) {
			Direction fromFacing = (Direction)stateFrom.get(FACING);
			if (fromFacing.equals(direction)) {
				return facing.equals(direction.getOpposite());
			}

			if (fromFacing.equals(direction.getOpposite())) {
				return facing.equals(direction);
			}

			if (fromFacing.equals(facing)) {
				return true;
			}
		}

		return super.isSideInvisible(state, stateFrom, direction);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
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
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		return world.getMaxLightLevel();
	}

	static {
		WATERLOGGED = Properties.WATERLOGGED;
		NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 12.0, 16.0, 16.0, 16.0);
		NORTH_COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
		EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 4.0, 16.0, 16.0);
		EAST_COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
		SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 4.0);
		SOUTH_COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
		WEST_SHAPE = Block.createCuboidShape(12.0, 0.0, 0.0, 16.0, 16.0, 16.0);
		WEST_COLLISION_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
		UP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
		UP_COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
		DOWN_SHAPE = Block.createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);
		DOWN_COLLISION_SHAPE = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
	}
}
