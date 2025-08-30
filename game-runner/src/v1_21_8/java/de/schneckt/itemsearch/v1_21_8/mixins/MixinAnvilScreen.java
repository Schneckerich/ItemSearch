package de.schneckt.itemsearch.v1_21_8.mixins;

import de.schneckt.itemsearch.ItemSearch;
import de.schneckt.itemsearch.v1_21_8.widgets.SearchWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreen.class)
public abstract class MixinAnvilScreen extends MixinAbstractContainerScreen {

    protected MixinAnvilScreen(Component title) {
        super(title);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void mixinKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        if (super.itemsearch$searchWidget.isFocused()) {
            cir.setReturnValue(super.keyPressed(keyCode, scanCode, modifiers));
        }
    }
}
