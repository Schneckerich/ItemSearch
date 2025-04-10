package de.schneckt.itemsearch.v1_21_5.widgets;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
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
        String itemIDstring = itemStack.getItem().toString().replace("minecraft:", "").
            replace("_", "");
        if (itemIDstring.equalsIgnoreCase("air")) return false;

        String itemDisplayName = itemStack.getDisplayName().getString().toLowerCase();
        return itemIDstring.contains(searchString.toLowerCase()) || itemDisplayName.contains(searchString.toLowerCase());
    }
}
