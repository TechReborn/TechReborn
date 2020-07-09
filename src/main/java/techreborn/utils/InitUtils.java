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

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import reborncore.RebornRegistry;
import team.reborn.energy.Energy;
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
		return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
	}

	public static void initPoweredItems(Item item, DefaultedList<ItemStack> itemList) {
		ItemStack uncharged = new ItemStack(item);
		ItemStack charged = new ItemStack(item);

		Energy.of(charged).set(Energy.of(charged).getMaxStored());

		itemList.add(uncharged);
		itemList.add(charged);
	}

	public static AbstractBlock.Settings setupRubberBlockSettings(boolean noCollision, float hardness, float resistance) {

		FabricBlockSettings settings = FabricBlockSettings.of(Material.WOOD, MaterialColor.SPRUCE);
		settings.strength(hardness, resistance);
		settings.sounds(BlockSoundGroup.WOOD);
		if (noCollision) {
			settings.noCollision();
		}
		settings.materialColor(MaterialColor.SPRUCE);
		return settings;
	}

	public static AbstractBlock.Settings setupRubberBlockSettings(float hardness, float resistance) {
		return setupRubberBlockSettings(false, hardness, resistance);
	}
}
