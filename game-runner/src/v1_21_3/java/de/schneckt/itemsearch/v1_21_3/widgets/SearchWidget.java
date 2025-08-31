package de.schneckt.itemsearch.v1_21_3.widgets;

import de.schneckt.itemsearch.ItemSearch;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.key.mapper.KeyMapper;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.core.client.gui.screen.key.mapper.DefaultKeyMapper;
import net.labymod.v1_21_3.client.gui.screen.widget.converter.TextFieldConverter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SearchWidget extends EditBox {

    private static final ItemSearch itemSearch = ItemSearch.getInstance();
    private static final SearchWidget instance = new SearchWidget();
    private final TextFieldConverter converter = new TextFieldConverter();
    public TextFieldWidget searchBoxWidget;
    public static String searchString = "";

    private SearchWidget() {
        super(Minecraft.getInstance().font, 0, 0, 120, 20, Component.literal("Search Box"));
        this.setValue(searchString);
        String boxHint = Laby.references().internationalization().getTranslation("itemsearch.searchWidget.boxHint");
        this.setHint(Component.literal(boxHint));
        this.setResponder((input) -> {
            searchString = input;
        });
        this.searchBoxWidget = (TextFieldWidget) Laby.references().widgetConverterRegistry().convertWidget(this, this.converter);
    }

    public boolean matchesSearch(ItemStack itemStack) {
        String itemIDstring = itemStack.getItem().toString().replace("minecraft:", "").
            replace("_", "");
        if (itemIDstring.equalsIgnoreCase("air")) return false;

        String itemDisplayName = itemStack.getDisplayName().getString().toLowerCase();
        return itemIDstring.contains(searchString.toLowerCase()) || itemDisplayName.contains(searchString.toLowerCase());
    }

    public static SearchWidget getInstance() {
        return instance;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.searchBoxWidget == null) {
            super.renderWidget(graphics, mouseX, mouseY, partialTicks);
            return;
        }
        this.converter.update(this, searchBoxWidget);
        Laby.labyAPI().minecraft().updateMouse((double)mouseX, (double)mouseY, (mouse) -> {
            ScreenContext screenContext = Laby.references().renderEnvironmentContext().screenContext();
            screenContext.runInContext(
                ((VanillaStackAccessor)graphics.pose()).stack(),
                mouse,
                partialTicks,
                this.searchBoxWidget::render
            );
        });
    }

    @Override
    public boolean charTyped(char c, int param) {
        if (this.searchBoxWidget == null) {
            return super.charTyped(c, param);
        }
        Key key = DefaultKeyMapper.lastPressed();
        return new TextFieldConverter().charTyped(this.searchBoxWidget, key, c);
    }

    @Override
    public boolean keyPressed(int keyCode, int param1, int param2) {
        if (this.searchBoxWidget == null) {
            return super.keyPressed(keyCode, param1, param2);
        }
        Key key = DefaultKeyMapper.lastPressed();
        return new TextFieldConverter().keyPressed(this.searchBoxWidget, key, KeyMapper.getInputType(key));
    }
}
