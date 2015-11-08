package techreborn.client.keybindings;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import techreborn.Core;
import techreborn.lib.Key;

public class KeyInputEventHandler {
    private static Key getPressedKeybinding() {
        if (KeyBindings.config.isPressed()) {
            return Key.CONFIG;
        }

        return Key.UNKNOWN;
    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
        Core.logHelper.info(getPressedKeybinding());
    }

}
