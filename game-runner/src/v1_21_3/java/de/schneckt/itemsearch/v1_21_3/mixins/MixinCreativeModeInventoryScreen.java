package de.schneckt.itemsearch.v1_21_3.mixins;

import de.schneckt.itemsearch.ItemSearch;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Type;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class MixinCreativeModeInventoryScreen extends MixinAbstractContainerScreen {

    protected MixinCreativeModeInventoryScreen(Component title) {
        super(title);
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void mixinCharTyped(char letter, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        if (super.itemsearch$searchWidget.isFocused()) {
            cir.setReturnValue(super.charTyped(letter, modifiers));
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void mixinKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        if (super.itemsearch$searchWidget.isFocused()) {
            cir.setReturnValue(super.keyPressed(keyCode, scanCode, modifiers));
        }
    }
}
