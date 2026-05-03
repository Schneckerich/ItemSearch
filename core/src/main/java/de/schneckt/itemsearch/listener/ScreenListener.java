package de.schneckt.itemsearch.listener;

import de.schneckt.itemsearch.ItemSearch;
import de.schneckt.itemsearch.activity.InventorySearchOverlay;
import de.schneckt.itemsearch.event.AbstractContainerScreenInitializedEvent;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.gui.screen.NamedScreen;
import net.labymod.api.client.gui.screen.ScreenWrapper;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.event.client.gui.screen.ScreenOpenEvent;
import net.labymod.api.event.client.gui.screen.VersionedScreenInitEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;

import static net.labymod.api.Laby.labyAPI;

public class ScreenListener {
    public static boolean init = false;

    public boolean isContainerScreen(Object versionedScreen) {
        String superName = versionedScreen.getClass().getSuperclass().getName();
        String superSuperName = versionedScreen.getClass().getSuperclass().getSuperclass().getName();
        return superName.contains("AbstractContainerScreen") ||
            superName.contains("AbstractFurnaceScreen") ||
            superSuperName.contains("AbstractContainerScreen");
    }

    @Subscribe
    public void onScreenOpen(AbstractContainerScreenInitializedEvent event) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;
        if (!labyAPI().minecraft().isIngame()) return;

        Minecraft minecraft = labyAPI().minecraft();
        ScreenWrapper screenWrapper = minecraft.minecraftWindow().currentScreen();
        if (screenWrapper == null) return;

        int imageHeight = event.getImageHeight();
        if (NamedScreen.CREATIVE_INVENTORY.isScreen(screenWrapper.getVersionedScreen())) {
            imageHeight = 160;
        }

        // if-statement needed to avoid infinite recursion
        if (isContainerScreen(screenWrapper.getVersionedScreen())) {
            InventorySearchOverlay inventorySearchOverlay = new InventorySearchOverlay(
                screenWrapper,
                event.getLeftPos(),
                event.getTopPos(),
                imageHeight
            );
            minecraft.minecraftWindow().displayScreen(inventorySearchOverlay);
        }
    }

}
