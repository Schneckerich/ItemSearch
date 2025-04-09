package de.schneckt.itemsearch.v1_21_4.mixins;

import de.schneckt.itemsearch.ItemSearch;
import de.schneckt.itemsearch.ItemSearchConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class MixinCreativeModeInventoryScreen extends AbstractContainerScreen{

    @Shadow
    private boolean hasClickedOutside;

    public MixinCreativeModeInventoryScreen(AbstractContainerMenu $$0, Inventory $$1,
        Component $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void mixinCharTyped(char letter, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;
        if (super.charTyped(letter, modifiers)) cir.setReturnValue(true);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void mixinKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (hasClickedOutside &&
            (Minecraft.getInstance().options.keyChat.matches(keyCode, scanCode) ||
                keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
            super.keyPressed(keyCode, scanCode, modifiers);
            cir.setReturnValue(true);
        }
    }
}
