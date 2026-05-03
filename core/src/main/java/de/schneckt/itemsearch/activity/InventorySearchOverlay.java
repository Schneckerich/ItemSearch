package de.schneckt.itemsearch.activity;

import de.schneckt.itemsearch.ItemSearch;
import de.schneckt.itemsearch.listener.ScreenListener;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.NamedScreen;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.AbstractLayerActivity.ParentMode;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.key.InputType;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.key.MouseButton;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

@Link("container-overlay.lss")
@AutoActivity
public class InventorySearchOverlay extends SimpleActivity {

    public TextFieldWidget searchWidget = null;
    protected ScreenInstance parentScreen;
    protected ParentMode mode = ParentMode.UNDERLAY;
    protected ParentMode renderMode = null;
    protected final int posX;
    protected final int posY;
    protected final int imageHeight;
    protected final int offset = 12;

    public InventorySearchOverlay(ScreenInstance parentScreen, int posX, int posY, int imageHeight) {
        this.parentScreen = parentScreen;
        this.posX = posX;
        this.posY = posY;
        this.imageHeight = imageHeight;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void initialize(Parent parent) {
        this.parentScreen.initialize(parent);
        super.initialize(parent);

        // Search field
        this.searchWidget = ItemSearch.getInstance().getSearchWidget();
        this.document().addChild(searchWidget);

        // Clear button
        ButtonWidget clearButton = new ButtonWidget();
        clearButton.addId("clear-button");
        clearButton.icon().set(SpriteCommon.TRASH);
        clearButton.setEnabled(true);
        clearButton.setPressable(() -> {
            this.searchWidget.setText("");
        });
        this.document().addChild(clearButton);

        this.document().setVariable("--pos-x", this.posX);
        this.document().setVariable("--pos-y", this.posY + this.imageHeight + this.offset);
        this.document().setVariable("--pos-x-clear", this.posX + 140);
        this.document().setVariable("--pos-y-clear", this.posY + this.imageHeight + this.offset);
    }

    @Override
    public boolean shouldRenderBackground() {
        return false;
    }

    @Override
    public void resize(int width, int height) {
        this.parentScreen.resize(width, height);
        super.resize(width, height);
    }

    @Override
    public void render(ScreenContext context) {
        context.pushStack();

        // Transform activity and parent screen for theme animations
        this.transformActivity(context);

        this.renderParent(context);
        this.renderSuper(context);

        context.popStack();
    }

    protected void renderSuper(ScreenContext environment) {
        super.render(environment);
    }

    protected void renderParent(ScreenContext environment) {
        this.parentScreen.render(environment);
    }

    @Override
    public void renderOverlay(ScreenContext context) {
        this.renderParentOverlay(context);
        super.renderOverlay(context);
    }

    protected void renderParentOverlay(ScreenContext context) {
        this.parentScreen.renderOverlay(context);
    }

    @Override
    public void onCloseScreen() {
        this.parentScreen.onCloseScreen();
        super.onCloseScreen();
    }

    @Override
    public void tick() {
        this.parentScreen.tick();
        super.tick();
    }

    @Override
    public boolean keyPressed(Key key, InputType type) {
        if (!this.searchWidget.isFocused() || key == Key.ESCAPE) {
            if (Laby.labyAPI().minecraft().isKeyPressed(Key.F) &&
                Laby.labyAPI().minecraft().isKeyPressed(Key.L_CONTROL)) {
                this.searchWidget.setFocused(true);
                return true;
            }
            return this.parentScreen.keyPressed(key, type);
        } else {
            if (key == Key.ENTER) {
                this.searchWidget.setFocused(false);
                return false;
            }
            return super.keyPressed(key, type);
        }
    }

    @Override
    public boolean keyReleased(Key key, InputType type) {
        if (this.mode == ParentMode.UNDERLAY
            && this.parentScreen.keyReleased(key, type)) {
            return true;
        }

        // The escape key should be handled by the parent screen
        if (super.keyReleased(key, type)) {
            return true;
        }

        if (this.mode == ParentMode.OVERLAY) {
            return this.parentScreen.keyReleased(key, type);
        }

        return false;
    }

    @Override
    public boolean charTyped(Key key, char character) {

        if (super.charTyped(key, character)) {
            return true;
        }

        return this.parentScreen.charTyped(key, character);
    }

    @Override
    public boolean mouseReleased(MutableMouse mouse, MouseButton mouseButton) {

        if (super.mouseReleased(mouse, mouseButton)) {
            return true;
        }

        return this.parentScreen.mouseReleased(mouse, mouseButton);

    }

    @Override
    public boolean mouseClicked(MutableMouse mouse, final MouseButton mouseButton) {

        if (super.mouseClicked(mouse, mouseButton)) {
            return true;
        }

        return this.parentScreen.mouseClicked(mouse, mouseButton);
    }

    @Override
    public boolean mouseScrolled(MutableMouse mouse, double scrollDelta) {
        if (this.mode == ParentMode.UNDERLAY
            && this.parentScreen.mouseScrolled(mouse, scrollDelta)) {
            return true;
        }

        if (super.mouseScrolled(mouse, scrollDelta)) {
            return true;
        }

        if (this.mode == ParentMode.OVERLAY) {
            return this.parentScreen.mouseScrolled(mouse, scrollDelta);
        }

        return false;
    }

    @Override
    public boolean mouseDragged(MutableMouse mouse, MouseButton button, double deltaX,
        double deltaY) {
        if (this.mode == ParentMode.UNDERLAY
            && this.parentScreen.mouseDragged(mouse, button, deltaX, deltaY)) {
            return true;
        }

        if (super.mouseDragged(mouse, button, deltaX, deltaY)) {
            return true;
        }

        if (this.mode == ParentMode.OVERLAY) {
            return this.parentScreen.mouseDragged(mouse, button, deltaX, deltaY);
        }

        return false;
    }

    @Override
    public void doScreenAction(String action, Map<String, Object> parameters) {
        if (this.mode == ParentMode.UNDERLAY) {
            this.parentScreen.doScreenAction(action, parameters);
        }

        super.doScreenAction(action, parameters);

        if (this.mode == ParentMode.OVERLAY) {
            this.parentScreen.doScreenAction(action, parameters);
        }
    }

    @Override
    @NotNull
    public Object mostInnerScreen() {
        return this.parentScreen.mostInnerScreen();
    }

    @Override
    @NotNull
    public ScreenInstance mostInnerScreenInstance() {
        return this.parentScreen.mostInnerScreenInstance();
    }

    @Override
    public boolean allowCustomFont() {
        return true;
    }
}

