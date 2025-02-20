package com.thatoneaiguy.archipelago.item;

import com.thatoneaiguy.archipelago.entity.ArchipelagoEntity;
import com.thatoneaiguy.archipelago.entity.RiftEntity;
import com.thatoneaiguy.archipelago.init.ArchipelagoEntities;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JarItem extends Item {
    public JarItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!player.isSneaking()) {
            if (stack.getOrCreateNbt().getInt("charge") > 0) {
                spawnEntity(player, world);
                stack.getOrCreateNbt().putInt("charge", stack.getOrCreateNbt().getInt("charge") - 1);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (player.isSneaking() && entity instanceof RiftEntity) {
            // Logic to discard the entity
            entity.remove(Entity.RemovalReason.DISCARDED);

            int charge = stack.getOrCreateNbt().getInt("charge");

            if (charge < 3) {
                stack.getOrCreateNbt().putInt("charge", charge + 1);
            }

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int charge = stack.getOrCreateNbt().getInt("charge");
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.literal("[Shift + Right click] to pick up a rift").formatted(Formatting.DARK_GRAY));
            tooltip.add(Text.literal("[Right click] to place a rift").formatted(Formatting.DARK_GRAY));
            tooltip.add(Text.literal(" "));
        } else {
            tooltip.add(Text.literal("Hold [Shift] for more info").formatted(Formatting.DARK_GRAY));
        }
        tooltip.add(Text.literal(charge + "/3" + " Rifts").formatted(Formatting.DARK_GRAY));
    }

    private void spawnEntity(PlayerEntity player, World world) {
        Vec3d lookDirection = player.getRotationVec(1);
        double x = player.getX() + lookDirection.x * 3;
        double y = player.getY() + player.getEyeHeight(EntityPose.STANDING) + lookDirection.y * 3; // Adjust height if necessary
        double z = player.getZ() + lookDirection.z * 3;

        RiftEntity entity = new RiftEntity(ArchipelagoEntities.RIFT_ENTITY_TYPE, world);
        entity.setPosition(x, y, z);
        while(entity.getBlockStateAtPos() != Blocks.AIR.getDefaultState()) {
            y++;
            entity.setPosition(x, y, z);
        }
        world.spawnEntity(entity);
    }
}
