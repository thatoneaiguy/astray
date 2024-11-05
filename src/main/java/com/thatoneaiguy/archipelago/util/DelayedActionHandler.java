package com.thatoneaiguy.archipelago.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DelayedActionHandler {
    private static final List<ScheduledAction> actions = new ArrayList<>();

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(DelayedActionHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {
        Iterator<ScheduledAction> iterator = actions.iterator();

        // Iterate through all scheduled actions
        while (iterator.hasNext()) {
            ScheduledAction action = iterator.next();

            // Decrement the tick count
            action.ticksRemaining--;

            // Execute the action if the delay has expired
            if (action.ticksRemaining <= 0) {
                action.delayedAction.executeDelayedAction();
                iterator.remove(); // Remove the action after execution
            }
        }
    }

    public static void addDelayedAction(DelayedAction delayedAction, int delayTicks) {
        actions.add(new ScheduledAction(delayedAction, delayTicks));
    }

    // Internal class to keep track of action and remaining ticks
    private static class ScheduledAction {
        DelayedAction delayedAction;
        int ticksRemaining;

        ScheduledAction(DelayedAction delayedAction, int delayTicks) {
            this.delayedAction = delayedAction;
            this.ticksRemaining = delayTicks;
        }
    }
}
