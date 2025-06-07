package com.thatoneaiguy.archipelago.util;

import net.minecraft.client.render.item.ItemRenderer;

public interface DelayedAction {
    void executeDelayedAction();


    /*
    * HOW TO USE THIS METHOD?
    * class ExampleDelayedAction implements DelayedAction {
    *    WHATEVER YOU NEED FOR THE ACTION
    *
    *    ExampleDelayedAction(WHATEVER YOU NEED FOR THE ACTION) {
    *        WHATEVER YOU NEED FOR THE ACTION
    *    }
    *
    *    @Override
    *    public void executeDelayedAction() {
    *        YOUR ACTION
    *    }
    * }
    *
     */
    default void scheduleDelay(int delayTicks) {
        DelayedActionHandler.addDelayedAction(this, delayTicks);
    }
}