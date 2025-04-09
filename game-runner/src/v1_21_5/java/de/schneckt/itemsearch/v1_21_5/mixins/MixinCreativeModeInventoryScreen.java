package de.schneckt.itemsearch.v1_21_5.mixins;

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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class MixinCreativeModeInventoryScreen extends AbstractContainerScreen{

    private boolean allowNativeSearchBox = false;

    @Shadow
    private static CreativeModeTab selectedTab;

    @Shadow
    private EditBox searchBox;

    @Shadow
    protected boolean checkTabClicked(CreativeModeTab $$0, double $$1, double $$2) {
        return false;
    }

    public MixinCreativeModeInventoryScreen(AbstractContainerMenu $$0, Inventory $$1,
        Component $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void mixinCharTyped(char letter, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        if (!this.allowNativeSearchBox) {
            super.charTyped(letter, modifiers);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mixinMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        // no changes needed in other tabs
        if (selectedTab.getType() != Type.SEARCH) {
            this.allowNativeSearchBox = true;
        }

        double posX = mouseX - (double)this.leftPos;
        double posY = mouseY - (double)this.topPos;

        if (this.checkTabClicked(CreativeModeTabs.searchTab(), posX, posY)) return;

        if (this.searchBox.mouseClicked(mouseX, mouseY, button) && !this.allowNativeSearchBox) {
            this.allowNativeSearchBox = true;
        } else if (this.allowNativeSearchBox) {
            this.searchBox.setCanLoseFocus(true);
            this.searchBox.setFocused(false);
            this.allowNativeSearchBox = false;
        }
    }

    @Inject(method = "selectTab", at = @At("RETURN"))
    private void selectTab(CreativeModeTab $$0, CallbackInfo ci) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        if (!this.allowNativeSearchBox) {
            this.searchBox.setCanLoseFocus(true);
            this.searchBox.setFocused(false);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void mixinKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        if (!this.allowNativeSearchBox) {
            super.keyPressed(keyCode, scanCode, modifiers);
            cir.setReturnValue(true);
        }
    }
}
