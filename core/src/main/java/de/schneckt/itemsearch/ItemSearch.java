package de.schneckt.itemsearch;

import de.schneckt.itemsearch.listener.ScreenListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.size.SizeType;
import net.labymod.api.client.gui.screen.widget.size.WidgetSide;
import net.labymod.api.client.gui.screen.widget.size.WidgetSize;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class ItemSearch extends LabyAddon<ItemSearchConfig> {

  private static ItemSearch instance;
  private TextFieldWidget searchWidget = null;

  @Override
  protected void enable() {
      registerSettingCategory();
      instance = this;
      this.registerListener(new ScreenListener());

      this.searchWidget = new TextFieldWidget();
      this.searchWidget.setSize(SizeType.MIN, WidgetSide.WIDTH, WidgetSize.fixed(120));
      this.searchWidget.placeholder(Component.translatable("itemsearch.searchWidget.boxHint"));
      this.searchWidget.addId("search-widget");
  }

  @Override
  protected Class<? extends ItemSearchConfig> configurationClass() {
    return ItemSearchConfig.class;
  }

  public static ItemSearch getInstance() {
    return instance;
  }

  public TextFieldWidget getSearchWidget() {
      return this.searchWidget;
  }

  public String getSearchString() {
      if (this.searchWidget == null) return "";
      return this.searchWidget.getText();
  }
}
