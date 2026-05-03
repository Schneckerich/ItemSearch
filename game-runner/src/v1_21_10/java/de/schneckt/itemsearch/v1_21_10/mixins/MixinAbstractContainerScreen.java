package de.schneckt.itemsearch.v1_21_10.mixins;

import de.schneckt.itemsearch.ItemSearch;
import de.schneckt.itemsearch.event.AbstractContainerScreenInitializedEvent;
import net.labymod.api.Laby;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {

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

        Laby.fireEvent(new AbstractContainerScreenInitializedEvent(this.leftPos, this.topPos, this.imageHeight));
    }

    @Unique
    public boolean itemsearch$matchesSearch(ItemStack itemStack, String searchString) {
        String itemIDstring = itemStack.getItem().toString().replace("minecraft:", "").
            replace("_", "");
        if (itemIDstring.equalsIgnoreCase("air")) return false;
        String itemDisplayName = itemStack.getDisplayName().getString().toLowerCase();
        return itemIDstring.contains(searchString.toLowerCase()) || itemDisplayName.contains(searchString.toLowerCase());
    }

    // dye the inventory/container slots
    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void mixinRenderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (!ItemSearch.getInstance().configuration().enabled().get()) return;
        ItemSearch itemSearch = ItemSearch.getInstance();
        if (!itemSearch.configuration().enabled().get()) return;
        if (slot == null || !slot.isHighlightable()) return;
        String searchString = itemSearch.getSearchString();
        if (searchString.isEmpty()) return;
        int color;

        if (itemsearch$matchesSearch(slot.getItem(), searchString)) {
            color = itemSearch.configuration().getMatchColor().get().get();
        } else {
            color = itemSearch.configuration().getMismatchColor().get().get();
        }

        if (!itemSearch.configuration().getSoftHighlight().get()) {
            guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                SLOT_HIGHLIGHT_BACK_SPRITE,
                slot.x - 4,
                slot.y - 4,
                24,
                24,
                color);
        }
        guiGraphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            SLOT_HIGHLIGHT_FRONT_SPRITE,
            slot.x - 4,
            slot.y - 4,
            24,
            24,
            color);

    }
}
