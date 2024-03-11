/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.client.keybindings;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import reborncore.client.ClientNetworkManager;
import techreborn.packets.ServerboundPackets;

public class KeyBindings {
	// Actual keybindings are in TechRebornClient
	public static final String CATEGORY = "key.techreborn.category";
	public static final String CONFIG = "key.techreborn.config";

	public static KeyBinding config = new KeyBinding(CONFIG, GLFW.GLFW_KEY_P, CATEGORY);

	public static KeyBinding suitNightVision;

	public static void registerKeys() {
		suitNightVision = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.techreborn.suitNightVision",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_N,
				CATEGORY));
	}

	public static void handleSuitNVToggle() {
		ClientNetworkManager.sendToServer(ServerboundPackets.createPacketToggleNV());
	}
}
