package de.schneckt.itemsearch;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.Color;

@ConfigName("settings")
public class ItemSearchConfig extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> useFancyTheme = new ConfigProperty<>(true);

    @ColorPickerSetting
    private final ConfigProperty<Color> matchColor = new ConfigProperty<>(Color.WHITE);

    @ColorPickerSetting
    private final ConfigProperty<Color> mismatchColor = new ConfigProperty<>(Color.BLACK);


    @Override
    public ConfigProperty<Boolean> enabled() { return this.enabled; }

    public ConfigProperty<Boolean> getUseFancyTheme() {
        return useFancyTheme;
    }

    public ConfigProperty<Color> getMatchColor() {
        return matchColor;
    }

    public ConfigProperty<Color> getMismatchColor() {
        return mismatchColor;
    }

}
