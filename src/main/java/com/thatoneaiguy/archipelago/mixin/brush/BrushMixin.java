package com.thatoneaiguy.archipelago.mixin.brush;

import com.thatoneaiguy.archipelago.util.Brushable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BrushItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushItem.class)
public class BrushMixin {

    @Unique
    BlockHitResult archipelago$blockHitResult;
    @Unique
    BlockState archipelago$blockState;

    @ModifyVariable(method = "usageTick", at = @At("STORE"), ordinal = 0)
    public BlockHitResult archipelago$captureBlockHitResult(BlockHitResult blockHitResult) {
        this.archipelago$blockHitResult = blockHitResult;
        return blockHitResult;
    }

    @ModifyVariable(method = "usageTick", at = @At("STORE"), ordinal = 0)
    public BlockState archipelago$captureBlockHitResult(BlockState blockState) {
        this.archipelago$blockState = blockState;
        return blockState;
    }

    @Redirect(method = "usageTick", at = @At(value = "INVOKE", target = "net/minecraft/world/World.isClient ()Z"))
    public boolean archipelago$cancelLogic(World world) {
        world.isClient();
        return true;
    }

    @Inject(method = "usageTick", at = @At(value = "INVOKE", target = "net/minecraft/world/World.isClient ()Z", shift = At.Shift.BEFORE))
    public void archipelago$extendBrush(World world, LivingEntity livingEntity2, ItemStack itemStack, int i, CallbackInfo info) {
        this.archipelago$extendedBrush(world, livingEntity2, itemStack, i);
    }

    @Unique
    private void archipelago$extendedBrush(World world, LivingEntity livingEntity2, ItemStack itemStack, int i) {
        if (!world.isClient && this.archipelago$blockHitResult != null && this.archipelago$blockState != null) {
            BlockPos blockPos = this.archipelago$blockHitResult.getBlockPos();
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            boolean hasBrushed = false;
            if (blockEntity instanceof Brushable brushable) {
                hasBrushed = brushable.brush(world.getTime(), world, (PlayerEntity) livingEntity2, this.archipelago$blockHitResult.getSide(), blockPos, this.archipelago$blockState);
            } else if (this.archipelago$blockState.getBlock() instanceof Brushable brushable) {
                hasBrushed = brushable.brush(world.getTime(), world, (PlayerEntity) livingEntity2, this.archipelago$blockHitResult.getSide(), blockPos, this.archipelago$blockState);
            }
            if (hasBrushed) {
                itemStack.damage(1, livingEntity2, livingEntity -> livingEntity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
        }
    }
}