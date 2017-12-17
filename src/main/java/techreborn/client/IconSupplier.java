/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IconSupplier {
	public static String armour_head_name = "techreborn:gui/slot_sprites/armour_head";
	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite armour_head;

	public static String armour_chest_name = "techreborn:gui/slot_sprites/armour_chest";
	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite armour_chest;

	public static String armour_legs_name = "techreborn:gui/slot_sprites/armour_legs";
	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite armour_legs;

	public static String armour_feet_name = "techreborn:gui/slot_sprites/armour_feet";
	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite armour_feet;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void preTextureStitch(TextureStitchEvent.Pre event) {
		TextureMap map  = event.getMap();
		armour_head = map.registerSprite(new ResourceLocation(armour_head_name));
		armour_chest = map.registerSprite(new ResourceLocation(armour_chest_name));
		armour_legs = map.registerSprite(new ResourceLocation(armour_legs_name));
		armour_feet = map.registerSprite(new ResourceLocation(armour_feet_name));
	}

}
