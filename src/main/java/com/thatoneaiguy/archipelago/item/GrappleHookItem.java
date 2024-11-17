package com.thatoneaiguy.archipelago.item;

import com.thatoneaiguy.archipelago.util.RaycastHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrappleHookItem extends Item {
    public GrappleHookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Ensure the user is a valid player
        if (user instanceof ClientPlayerEntity) {
            ClientPlayerEntity player = (ClientPlayerEntity) user;

            // Perform the raycast using the RaycastHelper class
            RaycastHelper.RaycastResult result = RaycastHelper.raycast(30);

            if (result != null) {
                BlockPos blockPos = result.getBlockPos();
                double distance = result.getDistance();

                // Send a message to the player about the block hit
                player.sendMessage(Text.of("Hit block at: " + blockPos + " with distance: " + distance), true);
            } else {
                // No block was hit within range
                player.sendMessage(Text.of("No block hit within range."), true);
            }
        }

        return TypedActionResult.success(user.getStackInHand(hand), true); // Return SUCCESS to indicate that the item use was handled successfully
    }
}
