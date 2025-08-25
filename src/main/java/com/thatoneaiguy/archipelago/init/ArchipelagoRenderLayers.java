package com.thatoneaiguy.archipelago.init;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class ArchipelagoRenderLayers extends RenderLayer {

    public static final RenderLayer TRAIL = RenderLayer.of(
            "archipelago:trail",
            VertexFormats.POSITION_COLOR,
            VertexFormat.DrawMode.TRIANGLES,
            256,
            false, true,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(RenderPhase.COLOR_PROGRAM)
                    .transparency(LIGHTNING_TRANSPARENCY)
                    .writeMaskState(COLOR_MASK)
                    .cull(DISABLE_CULLING)
                    .build(false)
    );

    public ArchipelagoRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer getTrail() {
        return TRAIL;
    }
}