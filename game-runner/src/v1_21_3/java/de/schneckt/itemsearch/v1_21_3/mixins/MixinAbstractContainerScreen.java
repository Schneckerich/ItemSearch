package de.schneckt.itemsearch.v1_21_3.mixins;

import de.schneckt.itemsearch.ItemSearch;
import de.schneckt.itemsearch.v1_21_3.widgets.SearchWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.ItemPickerMenu;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {

    protected SearchWidget searchWidget;

    @Shadow
    protected int leftPos;
    @Shadow
    protected int topPos;
    @Shadow
    protected int imageHeight;

    @Unique
    private static final ResourceLocation SLOT_HIGHLIGHT_BACK_SPRITE = ResourceLocation.withDefaultNamespace(
        "container/slot_highlight_back");
    @Unique
    private static final ResourceLocation SLOT_HIGHLIGHT_FRONT_SPRITE = ResourceLocation.withDefaultNamespace(
        "container/slot_highlight_front");

    protected MixinAbstractContainerScreen(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void mixinInit(CallbackInfo ci) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;
        LocalPlayer player = Minecraft.getInstance().player;

        int offset = 4;
        if (player.containerMenu instanceof ItemPickerMenu) offset = 28;

        this.searchWidget = new SearchWidget(this.leftPos + 1, this.topPos + this.imageHeight + offset);
        this.addWidget(this.searchWidget);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void mixinRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        this.searchWidget.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void mixinKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        if (!this.searchWidget.isFocused()) return;
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER) {
            this.setFocused(null);
            this.searchWidget.setFocused(false);
        }
        this.searchWidget.keyPressed(keyCode, scanCode, modifiers);
        cir.setReturnValue(true);
    }

    // exit search box when clicking outside
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mixinMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;

        if (this.searchWidget.mouseClicked(mouseX, mouseY, button)) {
            if (!this.searchWidget.isFocused()) this.searchWidget.setFocused(true);
            return;
        }
        if (this.searchWidget.isFocused()) this.searchWidget.setFocused(false);
    }


    // dye the inventory/container slots
    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void mixinRenderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;
        ItemSearch instance = ItemSearch.getInstance();
        if (!instance.configuration().enabled().get()) return;
        if (slot == null || !slot.isHighlightable()) return;
        if (SearchWidget.searchString.isEmpty()) return;
        int color;

        if (this.searchWidget.matchesSearch(slot.getItem())) {
            color = instance.configuration().getMatchColor().get().get();
        } else {
            color = instance.configuration().getMismatchColor().get().get();
        }

        guiGraphics.blitSprite(
            RenderType::guiTextured,
            SLOT_HIGHLIGHT_BACK_SPRITE,
            slot.x - 4,
            slot.y - 4,
            24,
            24,
            color);
        guiGraphics.blitSprite(
            RenderType::guiTexturedOverlay,
            SLOT_HIGHLIGHT_FRONT_SPRITE,
            slot.x - 4,
            slot.y - 4,
            24,
            24,
            color);

    }
}
