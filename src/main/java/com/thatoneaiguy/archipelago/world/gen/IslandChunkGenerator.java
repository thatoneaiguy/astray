package com.thatoneaiguy.archipelago.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.thatoneaiguy.archipelago.wrapper.NoiseSampler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class IslandChunkGenerator extends ChunkGenerator {
    private final NoiseSampler noiseSampler;
    private final long seed;

    public static final Codec<IslandChunkGenerator> CODEC = null; // Implement codec for JSON reference

    public IslandChunkGenerator(BiomeSource biomeSource, long seed) {
        super(biomeSource);
        this.seed = seed;
        this.noiseSampler = new NoiseSampler(seed);
    }

    public NoiseSampler getNoise() {
        return noiseSampler;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {

    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {

    }

    @Override
    public void populateEntities(ChunkRegion region) {

    }

    @Override
    public int getWorldHeight() { return 256; }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        return null;
    }

    @Override
    public int getSeaLevel() { return 0; }

    @Override
    public int getMinimumY() { return 0; }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        int height = getWorldHeight() - getMinimumY();
        BlockState[] blocks = new BlockState[height];
        Arrays.fill(blocks, Blocks.AIR.getDefaultState());

        int[] layers = {80, 140, 200};
        for (int baseY : layers) {
            double noise = noiseSampler.sample(x, z, baseY);
            if (noise > 0.65) {
                int top = baseY + (int)(noise * 20);
                for (int y = top; y > top - 6; y--) {
                    if (y >= 0 && y < height)
                        blocks[y] = Blocks.END_STONE.getDefaultState();
                }
            }
        }

        return new VerticalBlockSample(getMinimumY(), blocks);
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type type, HeightLimitView world, NoiseConfig noiseConfig) {
        int maxHeight = 0;
        int[] layers = {80, 140, 200};
        for (int baseY : layers) {
            double n = noiseSampler.sample(x, z, baseY);
            if (n > 0.65) maxHeight = Math.max(maxHeight, baseY + (int)(n * 20));
        }
        return maxHeight;
    }
    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {}
}