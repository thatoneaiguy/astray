package com.thatoneaiguy.archipelago.mixin.qol;

import com.thatoneaiguy.archipelago.item.Starweaver;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "attack")
	private void archipelago$AttackSounds(CallbackInfo ci) {

		PlayerEntity player = (PlayerEntity) (Object) this;
		float attackPower = player.getAttackCooldownProgress(.0f);
		ItemStack tool = player.getStackInHand(Hand.MAIN_HAND);

		if (tool.getItem() instanceof Starweaver) {
			if (attackPower > .8f) {
				float pitch = .9f + (player.getRandom().nextFloat() * .2f);
				player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 1.f, pitch);
				player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.PLAYERS, 1.f, pitch);
			}
		}
	}
}
