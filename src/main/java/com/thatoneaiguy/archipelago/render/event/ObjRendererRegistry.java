package com.thatoneaiguy.archipelago.render.event;

import com.thatoneaiguy.archipelago.Archipelago;
import net.minecraft.util.Identifier;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

public class ObjRendererRegistry {
    public static ObjModel star = new ObjModel(new Identifier(Archipelago.MODID, "obj/star"));
}
