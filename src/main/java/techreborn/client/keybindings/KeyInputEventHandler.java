package techreborn.client.keybindings;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
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
