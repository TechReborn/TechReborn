package techreborn.client.keybindings;

import techreborn.lib.Key;
import techreborn.util.LogHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyInputEventHandler 
{
	private static Key getPressedKeybinding()
	{
		if (KeyBindings.config.isPressed()) {
			return Key.CONFIG;
		}

		return Key.UNKNOWN;
	}

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event)
	{
		LogHelper.info(getPressedKeybinding());
	}

}
