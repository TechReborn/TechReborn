package techreborn.client.keybindings;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import techreborn.lib.ModInfo;

public class KeyBindings {

    public static KeyBinding config = new KeyBinding(ModInfo.Keys.CONFIG,
            Keyboard.KEY_P, ModInfo.Keys.CATEGORY);

}
