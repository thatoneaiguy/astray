package com.thatoneaiguy.archipelago.shader;

import com.thatoneaiguy.archipelago.Archipelago;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

public class RuptureShader extends PostProcessor {
    public static final RuptureShader INSTANCE = new RuptureShader();

    static {
        RuptureShader.INSTANCE.setActive(true);
    }

    @Override
    public Identifier getPostChainLocation() {
        return new Identifier(Archipelago.MODID, "rupture_post");
    }

    @Override
    public void beforeProcess(MatrixStack matrixStack) {

    }

    @Override
    public void afterProcess() {

    }
}
