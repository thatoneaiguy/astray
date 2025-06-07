package com.thatoneaiguy.archipelago.init;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;

public class ArchipelagoEntitySpawning {
    public static void registerRiftSpawning() {
        SpawnRestriction.register(ArchipelagoEntities.RIFT_ENTITY_TYPE,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.WORLD_SURFACE,
                (entityType1, world, spawnReason, pos, random) -> canSpawnHere(world, pos));
    }

    private static boolean canSpawnHere(WorldAccess world, BlockPos pos) {
        BlockPos groundPos = null;
        for (int y = pos.getY(); y >= pos.getY() - 5; y--) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState state = world.getBlockState(checkPos);
            if (state.isSolidBlock(world, checkPos)) {
                groundPos = checkPos;
                break;
            }
        }
        if (groundPos == null) {
            return false;
        }

        for (BlockPos checkPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            BlockState state = world.getBlockState(checkPos);
            if (!state.isAir() && !state.isReplaceable()) {
                return false;
            }
        }

        boolean hasFoliageNearby = false;
        for (BlockPos checkPos : BlockPos.iterate(groundPos.add(-8, -3, -8), groundPos.add(8, 3, 8))) {
            BlockState state = world.getBlockState(checkPos);
            if (state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.LOGS)) {
                hasFoliageNearby = true;
                break;
            }
        }
        if (!hasFoliageNearby) {
            return false;
        }

        int skyLight = world.getLightLevel(LightType.SKY, pos);
        int blockLight = world.getLightLevel(LightType.BLOCK, pos);
        if (skyLight < 8 && blockLight < 4) {
            return false;
        }

        if (world instanceof ServerWorldAccess serverWorld) {
            if (serverWorld.getRandom().nextInt(20) != 0) {
                return false;
            }
        }

        return true;
    }
}
