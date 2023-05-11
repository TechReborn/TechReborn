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

package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import reborncore.RebornRegistry;
import techreborn.TechReborn;

public class InitUtils {
	public static <I extends Item> I setup(I item, String name) {
		RebornRegistry.registerIdent(item, new Identifier(TechReborn.MOD_ID, name));
		return item;
	}

	public static <B extends Block> B setup(B block, String name) {
		RebornRegistry.registerIdent(block, new Identifier(TechReborn.MOD_ID, name));
		return block;
	}

	public static SoundEvent setup(String name) {
		Identifier identifier = new Identifier(TechReborn.MOD_ID, name);
		return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
	}

	public static boolean isDatagenRunning() {
		return System.getProperty("fabric-api.datagen") != null;
	}

	private InitUtils() {/* No instantiation. */}
}
