package com.thatoneaiguy.archipelago.util.runic;

import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import com.thatoneaiguy.archipelago.init.ArchipelagoEntities;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class RelikServerHandler {
    private static final Identifier CAST_SPELL_PACKET_ID = new Identifier("archipelago", "cast_spell");
    private static final Map<String, Spell> SPELL_REGISTRY = new HashMap<>();

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(CAST_SPELL_PACKET_ID, ((server, player, handler, buf, responseSender) -> {
            String combo = buf.readString();

            server.execute(() -> {
                Spell spell = SPELL_REGISTRY.get(combo);
                if ( spell != null ) {
                    spell.cast((ServerWorld) player.getWorld(), player);
                }
            });
        }));

        registerSpells();
    }

    private static void registerSpells() {
        registerSpell("L L L", ((world, user) -> {
            Vec3d direction = user.getRotationVector();
            TotemEntity totem = new TotemEntity(ArchipelagoEntities.TOTEM_ENTITY_TYPE, world);
            totem.updatePosition(user.getX() + direction.x, user.getY() + user.getHeight() / 2.0 + direction.y, user.getZ() + direction.z);
            totem.setVelocity(direction.multiply(2.0));
            totem.setOwner(user);
            world.spawnEntity(totem);

            TotemManager.addTotem(user, totem);
        }));
    }

    private static void registerSpell(String combo, Spell spell) {
        SPELL_REGISTRY.put(combo, spell);
    }
}
