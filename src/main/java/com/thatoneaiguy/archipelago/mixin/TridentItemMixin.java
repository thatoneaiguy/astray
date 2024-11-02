package com.thatoneaiguy.archipelago.mixin;

import com.thatoneaiguy.archipelago.init.ArchipelagoBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin {

    @Inject(at = @At("HEAD"), method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;")
    private void archipelago$dontStabMyGlass(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        HitResult hitResult = user.raycast(5.0D, 1.0F, false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos targetBlock = blockHitResult.getBlockPos();

            if (world.getBlockState(targetBlock).getBlock() == ArchipelagoBlocks.PRIVACY_GLASS ||world.getBlockState(targetBlock).getBlock() == ArchipelagoBlocks.PRIVACY_GLASS_PANEL ) {
                cir.setReturnValue(TypedActionResult.fail(user.getStackInHand(hand)));
            }
        }
    }
}
