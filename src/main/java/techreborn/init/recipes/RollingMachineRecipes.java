/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.init.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techreborn.api.TechRebornAPI;
import techreborn.lib.ModInfo;

/**
 * Created by Prospector
 */
public class RollingMachineRecipes extends RecipeMethods {
	public static void init() {
		register(new ResourceLocation(ModInfo.MOD_ID, "cupronickel_heating_coil"), getMaterial("cupronickel_heating_coil", 3, Type.PART), "NCN", "C C", "NCN", 'N', "ingotNickel", 'C', "ingotCopper");
		register(new ResourceLocation(ModInfo.MOD_ID, "nichrome_heating_coil"), getMaterial("nichrome_heating_coil", 2, Type.PART), " N ", "NCN", " N ", 'N', "ingotNickel", 'C', "ingotChrome");
		if (oresExist("ingotAluminum")) {
			register(new ResourceLocation(ModInfo.MOD_ID, "kanthal_heating_coil"), getMaterial("kanthal_heating_coil", 3, Type.PART), "RRR", "CAA", "CCA", 'R', "ingotRefinedIron", 'C', "ingotChrome", 'A', "ingotAluminum");
		}
		if (oresExist("ingotAluminium")) {
			register(new ResourceLocation(ModInfo.MOD_ID, "kanthal_heating_coil2"), getMaterial("kanthal_heating_coil", 3, Type.PART), "RRR", "CAA", "CCA", 'R', "ingotRefinedIron", 'C', "ingotChrome", 'A', "ingotAluminium");
		}
	// TODO: fix recipe
		//	register(new ResourceLocation(ModInfo.MOD_ID, "plateMagnalium"), getMaterial("magnalium", 3, Type.PLATE), "AAA", "MMM", "AAA", 'A', "ingotAluminum", 'M', "dustMagnesium");
		register(new ResourceLocation(ModInfo.MOD_ID, "rail"), getStack(Blocks.RAIL, 24), "I I", "ISI", "I I", 'I', "ingotIron", 'S', "stickWood");
		register(new ResourceLocation(ModInfo.MOD_ID, "gold_rail"), getStack(Blocks.GOLDEN_RAIL, 8), "I I", "ISI", "IRI", 'I', "ingotGold", 'S', "stickWood", 'R', "dustRedstone");
		register(new ResourceLocation(ModInfo.MOD_ID, "detector_rail"), getStack(Blocks.DETECTOR_RAIL, 8), "I I", "IPI", "IRI", 'I', "ingotIron", 'P', getStack(Blocks.STONE_PRESSURE_PLATE), 'R', "dustRedstone");
		register(new ResourceLocation(ModInfo.MOD_ID, "activator_rail"), getStack(Blocks.ACTIVATOR_RAIL, 8), "ISI", "IRI", "ISI", 'I', "ingotIron", 'S', "stickWood", 'R', getStack(Blocks.REDSTONE_TORCH));
		register(new ResourceLocation(ModInfo.MOD_ID, "iron_bars"), getStack(Blocks.IRON_BARS, 24), "III", "III", 'I', "ingotIron");
		register(new ResourceLocation(ModInfo.MOD_ID, "iron_door"), getStack(Items.IRON_DOOR, 4), "II ", "II ", "II ", 'I', "ingotIron");
		register(new ResourceLocation(ModInfo.MOD_ID, "minecart"), getStack(Items.MINECART, 2), "I I", "III", 'I', "ingotIron");
		register(new ResourceLocation(ModInfo.MOD_ID, "bucket"), getStack(Items.BUCKET, 2), "I I", "I I", " I ", 'I', "ingotIron");
		register(new ResourceLocation(ModInfo.MOD_ID, "tripwire_hook"), getStack(Blocks.TRIPWIRE_HOOK, 4), " I ", " S ", " W ", 'I', "ingotIron", 'S', "stickWood", 'W', "plankWood");
		register(new ResourceLocation(ModInfo.MOD_ID, "heavy_pressure_plate"), getStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, 2), "II ", 'I', "ingotIron");
		register(new ResourceLocation(ModInfo.MOD_ID, "light_pressure_plate"), getStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, 2), "GG ", 'G', "ingotGold");
	}

	static void register(ResourceLocation resourceLocation, ItemStack output, Object... componentsObjects) {
		TechRebornAPI.addRollingOreMachinceRecipe(resourceLocation, output, componentsObjects);
	}
}
