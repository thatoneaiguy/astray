package com.thatoneaiguy.archipelago.block;

import com.thatoneaiguy.archipelago.init.ArchipelagoBlocks;
import com.thatoneaiguy.archipelago.init.ArchipelagoParticles;
import com.thatoneaiguy.archipelago.util.Brushable;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ResentmentSink extends HorizontalFacingBlock implements Brushable {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    private static final Map<BlockPos, Long> lastTriggeredTime = new HashMap<>();
    private static final long COOLDOWN_TIME = 30000;
    public static final BooleanProperty GENERATED = Properties.ENABLED;

    public ResentmentSink(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState().with(GENERATED, false)));
    }

    //added by winter to test if this would fix the model's renderer being bugged
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // return SHAPE;
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0, 1, 1.25, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.2, 1.25, 0.1875, 0.8125, 1.3, 0.8125));
        return shape;
    }


    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }


    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    private static Block searchFor = Blocks.SCULK_SHRIEKER;

    /*public static void Ring(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        long currentTime = System.currentTimeMillis();

        if (lastTriggeredTime.containsKey(pos) && (currentTime - lastTriggeredTime.get(pos)) < COOLDOWN_TIME) {
            return;
        }

        lastTriggeredTime.put(pos, currentTime);
        ((ServerWorld) world).spawnParticles(ArchipelagoParticles.VASE_BREAK_PARTICLE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 0.0);

        int searchRadius = 5;

        BlockPos.stream(pos.add(-searchRadius, -searchRadius, -searchRadius), pos.add(searchRadius, searchRadius, searchRadius))
                .forEach(blockPos -> {
                    BlockState blockState = world.getBlockState(blockPos);
                    if (blockState.getBlock() == ArchipelagoBlocks.ASTRAL_VASE && !blockPos.equals(pos)) {
                        ((ServerWorld) world).getBlockEntity(blockPos, BlockEntityType.SCULK_SHRIEKER).ifPresent(blockEntity -> {
                            blockEntity.shriek((ServerWorld) world, (ServerPlayerEntity) player);
                        });

                        ((ServerWorld) world).spawnParticles(ArchipelagoParticles.VASE_BREAK_PARTICLE, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 0.0);
                        Ring(world, blockPos, blockState, player);
                    }
                });
    }*/

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        player.incrementStat(Stats.MINED.getOrCreateStat(this));
        player.addExhaustion(0.005F);
        world.playSound(player, pos, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 1, 1);
        if (world instanceof ServerWorld serverWorld) {
            if (state.get(GENERATED)) {
                world.playSound(player, pos, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 1, 1);
                //((ServerWorld) world).spawnParticles(ArchipelagoParticles.VASE_BREAK_PARTICLE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0.0, 0.0, 0.0, 0.0);
                //this.Ring(world, pos, state, player);
            }
            StatusEffectInstance darkness = new StatusEffectInstance(StatusEffects.DARKNESS, 200, 0, false, false);
            player.addStatusEffect(darkness);
            //serverWorld.addParticle(ArchipelagoParticles.VASE_BREAK_PARTICLE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
            spawnLavaSpitParticles(world, pos.toCenterPos());
        }
        super.onBreak(world, pos, state, player);
    }

    public static void spawnCircularParticles(World world, Vec3d pos, int PARTICLE_COUNT, float EXPANSION_SPEED, Color startingColor, Color endingColor) {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double angle = (2 * Math.PI * i) / PARTICLE_COUNT;
            double xVelocity = Math.cos(angle) * EXPANSION_SPEED;
            double zVelocity = Math.sin(angle) * EXPANSION_SPEED;

            WorldParticleBuilder.create(LodestoneParticleRegistry.SPARKLE_PARTICLE)
                    .setScaleData(GenericParticleData.create(0.5f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                    .setColorData(ColorParticleData.create(startingColor, endingColor)
                            .setCoefficient(1.4f)
                            .setEasing(Easing.BOUNCE_IN_OUT)
                            .build())
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f)
                            .setSpinOffset((world.getTime() * 0.2f) % 6.28f)
                            .setEasing(Easing.QUARTIC_IN)
                            .build())
                    .setLifetime(40)
                    .setMotion(xVelocity, 0.01f, zVelocity) // Starts stable
                    .setGravityStrength(-0.05f) // Negative gravity makes it rise over time
                    .enableNoClip()
                    .spawn(world, pos.x, pos.y, pos.z);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(GENERATED);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack itemStack) {
        //if (player==null) do {

        // if (!world.isAir(pos.add((double) 0, (double) 1, (double) 0))) do {
        //     world.

        //     break;
        //} while (!world.isAir(pos.add((double) 0, (double) 1, (double) 0)));
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(GENERATED, false)));
        // break;
        //} while(player==null);
    }

    @Override
    public boolean brush(long l, World world, PlayerEntity player, Direction direction, BlockPos pos, BlockState state) {
        if (!state.get(GENERATED)) {
            return false;
        }

        if (!world.isClient) {
            world.setBlockState(pos, state.with(GENERATED, false), 3);
            spawnLootTableDrops((ServerWorld) world, pos, new Identifier("archipelago", "brushing/resentment_sink"), world.getRandom());

            int searchRadius = 32;

            BlockPos.stream(pos.add(-searchRadius, -searchRadius, -searchRadius), pos.add(searchRadius, searchRadius, searchRadius))
                    .forEach(blockPos -> {
                        BlockState blockState = world.getBlockState(blockPos);
                        if (blockState.getBlock() == ArchipelagoBlocks.RESENTMENT_SINK && !blockPos.equals(pos)) {
                            ((ServerWorld) world).getBlockEntity(blockPos, BlockEntityType.SCULK_SHRIEKER).ifPresent(blockEntity -> {
                                blockEntity.shriek((ServerWorld) world, (ServerPlayerEntity) player);
                            });
                        }
                    });
        }
        Color startingColor = new Color(50, 0, 100);
        Color endingColor = new Color(0, 0, 255);

        spawnCircularParticles(world, pos.toCenterPos(), 800, 0.3f, startingColor, endingColor);
        spawnLavaSpitParticles(world, pos.toCenterPos());

        return true;
    }

    public static void spawnLavaSpitParticles(World level, Vec3d pos) {
        Color startingColor = new Color(128, 0, 128); // Purple color
        Color endingColor = new Color(200, 100, 200);   // Lighter purple

        WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                .setScaleData(GenericParticleData.create(0.5f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.9f, 0.3f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor)
                        .setCoefficient(1.2f)
                        .setEasing(Easing.BOUNCE_IN_OUT)
                        .build())
                .setSpinData(SpinParticleData.create(0.5f, 1.0f)
                        .setSpinOffset((level.getTime() * 0.2f) % 6.28f)
                        .setEasing(Easing.QUARTIC_IN)
                        .build())
                .setLifetime(30)
                .addMotion(0, 0.3f, 0)
                .addMotion(0, -0.01f, 0)
                .enableNoClip()
                .spawn(level, pos.x, pos.y, pos.z);
    }

    public static void spawnLootTableDrops(ServerWorld world, BlockPos pos, Identifier lootTableId, Random random) {
        LootTable lootTable = world.getServer().getLootManager().getLootTable(lootTableId);
        LootContextParameterSet lootContext = new LootContextParameterSet.Builder(world)
                .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                .luck(1.0f)
                .build(LootContextTypes.CHEST);

        List<ItemStack> loot = lootTable.generateLoot(lootContext);
        if (loot.isEmpty()) {
            System.out.println("Loot generated is empty."); // Debugging line
        }

        for (ItemStack stack : loot) {
            if (!stack.isEmpty()) {
                spawnRandomItemEntity(world, pos, stack, random);
            }
        }
    }

    private static void spawnRandomItemEntity(ServerWorld world, BlockPos pos, ItemStack stack, Random random) {
        double xOffset = (random.nextDouble() - 0.5) * 2.0;
        double yOffset = random.nextDouble() * 0.5 + 0.2;
        double zOffset = (random.nextDouble() - 0.5) * 2.0;

        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5 + xOffset, pos.getY() + yOffset, pos.getZ() + 0.5 + zOffset, stack);
        itemEntity.setVelocity((random.nextDouble() - 0.5) * 0.2, 0.2, (random.nextDouble() - 0.5) * 0.2);

        world.spawnEntity(itemEntity);
    }
}
