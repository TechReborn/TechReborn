/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import reborncore.RebornCore;

public class IconSupplier {

	public static Identifier armour_head_id = new Identifier(RebornCore.MOD_ID, "gui/slot_sprites/armour_head");
	public static Identifier armour_chest_id = new Identifier(RebornCore.MOD_ID, "gui/slot_sprites/armour_chest");
	public static Identifier armour_legs_id = new Identifier(RebornCore.MOD_ID, "gui/slot_sprites/armour_legs");
	public static Identifier armour_feet_id = new Identifier(RebornCore.MOD_ID, "gui/slot_sprites/armour_feet");

	@Environment(EnvType.CLIENT)
	public static void registerSprites(SpriteAtlasTexture atlasTexture, ClientSpriteRegistryCallback.Registry registry) {
		registry.register(IconSupplier.armour_head_id);
		registry.register(IconSupplier.armour_chest_id);
		registry.register(IconSupplier.armour_legs_id);
		registry.register(IconSupplier.armour_feet_id);
	}
}
