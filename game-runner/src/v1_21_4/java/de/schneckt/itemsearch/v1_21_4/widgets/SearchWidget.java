package de.schneckt.itemsearch.v1_21_4.widget;

import de.schneckt.itemsearch.ItemSearch;
import net.labymod.api.Laby;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SearchWidget extends EditBox {
    public static String searchString = "";

    public SearchWidget(int x, int y) {
        super(Minecraft.getInstance().font, x, y, 120, 20, Component.literal("Search Box"));
        this.setValue(searchString);
        String boxHint = Laby.references().internationalization().getTranslation("itemsearch.searchWidget.boxHint");
        this.setHint(Component.literal(boxHint));
        this.setResponder((input) -> {
            searchString = input;
        });
    }

    public boolean matchesSearch(ItemStack itemStack) {
        String itemName = itemStack.getItem().getName().getString().toLowerCase();
        String itemDisplayName = itemStack.getDisplayName().getString().toLowerCase();

        return itemName.contains(searchString.toLowerCase()) || itemDisplayName.contains(searchString.toLowerCase());
    }


}
