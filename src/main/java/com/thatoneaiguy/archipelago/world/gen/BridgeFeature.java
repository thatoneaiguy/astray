package com.thatoneaiguy.archipelago.world.gen;

import com.mojang.serialization.Codec;
import com.thatoneaiguy.archipelago.wrapper.NoiseSampler;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BridgeFeature extends Feature<DefaultFeatureConfig> {
    private NoiseSampler noiseSampler;

    public BridgeFeature(Codec<DefaultFeatureConfig> codec, long seed) {
        super(codec);
        this.noiseSampler = new NoiseSampler(seed);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        WorldAccess world = ctx.getWorld();
        Random random = ctx.getRandom();
        long seed = ctx.getWorld().getSeed();  // get world seed dynamically
        if (noiseSampler == null) noiseSampler = new NoiseSampler(seed);

        BlockPos start = ctx.getOrigin();
        BlockPos end = findNearbyIsland(start, random);
        if (end == null || random.nextFloat() < 0.3f) return false;

        int dx = end.getX() - start.getX();
        int dz = end.getZ() - start.getZ();
        int steps = Math.max(Math.abs(dx), Math.abs(dz));

        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            int x = start.getX() + (int)(dx * t);
            int z = start.getZ() + (int)(dz * t);
            int y = (int)(MathHelper.lerp(t, start.getY(), end.getY()) + Math.sin(t * Math.PI) * 3);

            if (world instanceof StructureWorldAccess swa) {
                swa.setBlockState(new BlockPos(x, y, z), Blocks.END_STONE_BRICKS.getDefaultState(), 3);
            }
        }

        return true;
    }

    private BlockPos findNearbyIsland(BlockPos origin, Random random) {
        int searchRadius = 64;
        int attempts = 50;

        for (int i = 0; i < attempts; i++) {
            int dx = random.nextInt(searchRadius * 2) - searchRadius;
            int dz = random.nextInt(searchRadius * 2) - searchRadius;
            int x = origin.getX() + dx;
            int z = origin.getZ() + dz;

            for (int baseY : new int[]{80, 140, 200}) {
                double noise = noiseSampler.sample(x, z, baseY);
                if (noise > 0.65) {
                    int y = baseY + (int)(noise * 20);
                    return new BlockPos(x, y, z);
                }
            }
        }

        return null;
    }
}
