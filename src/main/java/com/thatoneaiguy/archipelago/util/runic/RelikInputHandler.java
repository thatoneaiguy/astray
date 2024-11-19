package com.thatoneaiguy.archipelago.util.runic;

import com.thatoneaiguy.archipelago.init.ArchipelagoItems;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.message.MessageType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RelikInputHandler {
    private static final int MAX_SEQUENCE_LENGTH = 3;
    private static final Identifier CAST_SPELL_PACKET_ID = new Identifier("archipelago", "cast_spell");

    private final LinkedList<Character> inputSequence = new LinkedList<>();
    private final Map<String, Spell> spellRegistry = new HashMap<>();
    private boolean leftClickPressed = false;
    private boolean rightClickPressed = false;

    boolean cast = false;

    public RelikInputHandler() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> handleMouseInput(client));

        registerSpell("L L L", (world, user) -> {
            // Totem throwing spell
        });
    }

    private void handleMouseInput(MinecraftClient client) {
        if (client.player == null) return;

        ItemStack mainHandStack = client.player.getStackInHand(Hand.MAIN_HAND);
        if (!isRelik(mainHandStack)) {
            resetCombo();
            return;
        }

        // Check for mouse input and add it to the combo sequence
        if (client.options.attackKey.isPressed()) {
            if (!leftClickPressed) addInput('L');
            leftClickPressed = true;
        } else {
            leftClickPressed = false;
        }

        if (client.options.useKey.isPressed()) {
            if (!rightClickPressed) addInput('R');
            rightClickPressed = true;
        } else {
            rightClickPressed = false;
        }

        // Reset the combo when it exceeds MAX_SEQUENCE_LENGTH
        if (inputSequence.size() > MAX_SEQUENCE_LENGTH) {
            resetCombo();
        }

        // When the combo reaches MAX_SEQUENCE_LENGTH, check if it's a valid spell
        if (inputSequence.size() == MAX_SEQUENCE_LENGTH) {
            String combo = String.join(" ", inputSequence.stream().map(String::valueOf).toArray(String[]::new));
            if (spellRegistry.containsKey(combo)) {
                sendSpellCastToServer(combo);
                cast = true;
                updateActionBarDisplay(client);
                resetCombo(); // Reset combo after spell is cast
            }
        }

        // Update the action bar display after detecting input
        updateActionBarDisplay(client);
    }

    private boolean isRelik(ItemStack stack) {
        return stack.getItem() == ArchipelagoItems.RELIK;
    }

    private void addInput(char input) {
        inputSequence.add(input);
        if ( inputSequence.size() > MAX_SEQUENCE_LENGTH) {
            resetCombo();
        }
    }

    private void resetCombo() {
        inputSequence.clear();
        cast = false;
    }

    private void sendSpellCastToServer(String combo) {
        PacketByteBuf buf = new PacketByteBuf(io.netty.buffer.Unpooled.buffer());
        buf.writeString(combo);
        ClientPlayNetworking.send(CAST_SPELL_PACKET_ID, buf);
    }

    private void registerSpell(String combo, Spell spell) {
        spellRegistry.put(combo, spell);
    }

    private void updateActionBarDisplay(MinecraftClient client) {
        if (inputSequence.isEmpty()) {
            return; // No sequence, no need to display anything
        }

        StringBuilder comboBuilder = new StringBuilder("-- ");
        for (char input : inputSequence) {
            comboBuilder.append(input).append(" ");
        }
        comboBuilder.append("--");
        String comboDisplay = comboBuilder.toString().trim();

        if (!cast) client.player.sendMessage(Text.literal(comboDisplay).formatted(Formatting.DARK_GREEN), true);
        else client.player.sendMessage(Text.literal(comboDisplay).formatted(Formatting.GREEN), true);
    }
}
