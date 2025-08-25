package com.thatoneaiguy.archipelago.wrapper;

import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;

public class NoiseSampler {
    private final PerlinNoiseSampler perlin;

    /**
     * Construct a noise sampler seeded from the world seed
     * @param seed world seed
     */
    public NoiseSampler(long seed) {
        Random mcRandom = Random.create(seed);
        ChunkRandom chunkRandom = new ChunkRandom(mcRandom);
        this.perlin = new PerlinNoiseSampler(chunkRandom);
    }

    /**
     * Sample Perlin noise for a given x, z, and vertical offset
     */
    public double sample(int x, int z, int yOffset) {
        double scale = 0.01;
        double nx = x * scale;
        double nz = z * scale;
        double offset = yOffset * 0.05;

        double total = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;
        double max = 0.0;

        for (int octave = 0; octave < 4; octave++) {
            double n = perlin.sample(nx * frequency, offset, nz * frequency);
            total += n * amplitude;
            max += amplitude;
            amplitude *= 0.5;
            frequency *= 2.0;
        }

        return total / max;
    }
}

