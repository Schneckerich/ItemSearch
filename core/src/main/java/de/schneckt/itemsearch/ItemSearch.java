package de.schneckt.itemsearch;

import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class ItemSearch extends LabyAddon<ItemSearchConfig> {

  private static ItemSearch instance;

  @Override
  protected void enable() {
      registerSettingCategory();
      instance = this;
  }

  @Override
  protected Class<? extends ItemSearchConfig> configurationClass() {
    return ItemSearchConfig.class;
  }

  public static ItemSearch getInstance() {
    return instance;
  }
}
