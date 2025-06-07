package com.thatoneaiguy.archipelago.mixin.client;

import com.thatoneaiguy.archipelago.init.ArchipelagoItems;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> {

    @Shadow
    public @Final ModelPart head;

    @Shadow
    public @Final ModelPart body;

    @Shadow
    public @Final ModelPart rightArm;

    @Shadow
    public @Final ModelPart leftArm;

    @Inject(method = { "positionRightArm",
            "positionLeftArm" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/CrossbowPosing;hold(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Z)V", shift = At.Shift.AFTER), cancellable = true)
    public void poseArms(T entity, CallbackInfo ci) {
        float f;

        //boolean twoHandedLeft = entity.getMainHandStack().getItem() == ArchipelagoItems.ASTROLOGICAL_GREATSWORD || entity.getMainHandStack().getItem() == ArchipelagoItems.ASTROLOGICAL_BROADSWORD;

        //boolean twoHandedRight = entity.getOffHandStack().getItem() == ArchipelagoItems.ASTROLOGICAL_GREATSWORD || entity.getOffHandStack().getItem() == ArchipelagoItems.ASTROLOGICAL_BROADSWORD;

        /*if (twoHandedLeft) {
            this.rightArm.pivotZ = MathHelper.sin(this.body.yaw) * 5.0f;
            this.rightArm.pivotX = -MathHelper.cos(this.body.yaw) * 5.0f;
            this.leftArm.pivotZ = -MathHelper.sin(this.body.yaw) * 5.0f;
            this.leftArm.pivotX = MathHelper.cos(this.body.yaw) * 5.0f;

            if (this.handSwingProgress >= 0.0f) {
                this.rightArm.yaw = (float) (-Math.PI / 8);
                this.rightArm.pitch = (float) (-(Math.PI * 0.30));
                this.leftArm.yaw = (float) (Math.PI / 4);
                this.leftArm.pitch = (float) (-(Math.PI * 0.30));

                f = 1.0f - this.handSwingProgress;
                f *= f;
                f *= f;
                f = 1.0f - f;
                float g = MathHelper.sin(f * (float) Math.PI);
                float h = MathHelper.sin(this.handSwingProgress * (float) Math.PI) * -(this.head.pitch - 0.7f) * 0.75f;
                this.leftArm.pitch -= g * 1.2f + h;
                this.leftArm.yaw += this.body.yaw * 2.0f;
                this.leftArm.roll += MathHelper.sin(this.handSwingProgress * (float) Math.PI) * -0.4f;
            }
            ci.cancel();
        }
        if (twoHandedRight) {

            this.rightArm.pivotZ = MathHelper.sin(this.body.yaw) * 5.0f;
            this.rightArm.pivotX = -MathHelper.cos(this.body.yaw) * 5.0f;
            this.leftArm.pivotZ = -MathHelper.sin(this.body.yaw) * 5.0f;
            this.leftArm.pivotX = MathHelper.cos(this.body.yaw) * 5.0f;

            if (this.handSwingProgress >= 0.0f) {
                this.rightArm.yaw = (float) -(Math.PI / 4);
                this.rightArm.pitch = (float) (-(Math.PI * 0.30));
                this.leftArm.yaw = (float) (Math.PI / 8);
                this.leftArm.pitch = (float) (-(Math.PI * 0.30));

                f = 1.0f - this.handSwingProgress;
                f *= f;
                f *= f;
                f = 1.0f - f;
                float g = MathHelper.sin(f * (float) Math.PI);
                float h = MathHelper.sin(this.handSwingProgress * (float) Math.PI) * -(this.head.pitch - 0.7f) * 0.75f;
                this.leftArm.pitch -= g * 1.2f + h;
                this.leftArm.yaw += this.body.yaw * 2.0f;
                this.leftArm.roll += MathHelper.sin(this.handSwingProgress * (float) Math.PI) * -0.4f;
            }
            ci.cancel();
        }*/
    }
}
