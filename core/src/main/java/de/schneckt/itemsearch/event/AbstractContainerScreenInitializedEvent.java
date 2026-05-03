package de.schneckt.itemsearch.event;

import net.labymod.api.event.Event;

/**
 * Created by Felix on 03.05.2026.
 */
public class AbstractContainerScreenInitializedEvent implements Event {

    private final int leftPos;
    private final int topPos;
    private final int imageHeight;

    public AbstractContainerScreenInitializedEvent(int leftPos, int topPos, int imageHeight) {
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.imageHeight = imageHeight;
    }

    public int getLeftPos() {
        return leftPos;
    }

    public int getTopPos() {
        return topPos;
    }

    public int getImageHeight() {
        return imageHeight;
    }
}
