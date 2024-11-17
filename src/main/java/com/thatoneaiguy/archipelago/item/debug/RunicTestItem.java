package com.thatoneaiguy.archipelago.item.debug;

import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import com.thatoneaiguy.archipelago.init.ArchipelagoEntities;
import com.thatoneaiguy.archipelago.util.TotemManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RunicTestItem extends Item {
    int mode = 1;

    public RunicTestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if ( mode == 1 ) {
            if (user instanceof ServerPlayerEntity) {
                Vec3d direction = user.getRotationVector();
                TotemEntity totem = new TotemEntity(ArchipelagoEntities.TOTEM_ENTITY_TYPE, world);
                totem.updatePosition(user.getX() + direction.x, user.getY() + user.getHeight() / 2.0 + direction.y, user.getZ() + direction.z);
                totem.setVelocity(direction.multiply(2.0));
                totem.setOwner((ServerPlayerEntity) user);
                world.spawnEntity(totem);

                TotemManager.addTotem((ServerPlayerEntity) user, totem);

                if (!user.getAbilities().creativeMode) {
                    user.getStackInHand(hand).decrement(1);
                }

                return TypedActionResult.success(user.getMainHandStack(), true);
            }
        }
        return TypedActionResult.fail(user.getMainHandStack());
    }

    /*@Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if ( mode < 1 || mode > 2) {
            mode = 1;
        } else {
            mode++;
        }

        return super.useOnBlock(context);
    }*/
}
