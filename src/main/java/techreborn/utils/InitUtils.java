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

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import reborncore.RebornRegistry;
import techreborn.TechReborn;

public class InitUtils {
	public static <I extends Item> I setup(I item, String name) {
		RebornRegistry.registerIdent(item, Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name));

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			String expect = Util.makeDescriptionId("item", Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name));
			String actual = item.getDescriptionId();

			if (!expect.equals(actual)) {
				// This happens when the item settings registry key does not match key used to register the item
				throw new IllegalStateException("Item translation key mismatch: expected " + expect + ", got " + actual);
			}
		}

		return item;
	}

	public static <B extends Block> B setup(B block, String name) {
		RebornRegistry.registerIdent(block, Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name));

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			String expect = Util.makeDescriptionId("block", Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name));
			String actual = block.getDescriptionId();

			if (!expect.equals(actual)) {
				// This happens when the block settings registry key does not match key used to register the block
				throw new IllegalStateException("Block translation key mismatch: expected " + expect + ", got " + actual);
			}
		}

		return block;
	}

	public static SoundEvent setup(String name) {
		Identifier identifier = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
	}

	public static boolean isDatagenRunning() {
		return System.getProperty("fabric-api.datagen") != null;
	}

	private InitUtils() {/* No instantiation. */}
}
