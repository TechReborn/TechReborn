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

package techreborn.init.recipes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.ScrapboxList;
import techreborn.api.recipe.ScrapboxRecipe;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.DynamicCell;
import techreborn.items.ItemDusts;
import techreborn.items.ItemGems;
import techreborn.items.ItemNuggets;
import techreborn.utils.StackWIPHandler;

/**
 * Created by Prospector
 */
public class ScrapboxRecipes extends RecipeMethods {
	public static void init() {
		register(getStack(Items.DIAMOND));
		register(getStack(Items.STICK));
		register(getStack(Items.COAL));
		register(getStack(Items.APPLE));
		register(getStack(Items.BAKED_POTATO));
		register(getStack(Items.BLAZE_POWDER));
		register(getStack(Items.WHEAT));
		register(getStack(Items.CARROT));
		register(getStack(Items.BOAT));
		register(getStack(Items.ACACIA_BOAT));
		register(getStack(Items.BIRCH_BOAT));
		register(getStack(Items.DARK_OAK_BOAT));
		register(getStack(Items.JUNGLE_BOAT));
		register(getStack(Items.SPRUCE_BOAT));
		register(getStack(Items.BLAZE_ROD));
		register(getStack(Items.COMPASS));
		register(getStack(Items.MAP));
		register(getStack(Items.LEATHER_LEGGINGS));
		register(getStack(Items.BOW));
		register(getStack(Items.COOKED_CHICKEN));
		register(getStack(Items.CAKE));
		register(getStack(Items.ACACIA_DOOR));
		register(getStack(Items.DARK_OAK_DOOR));
		register(getStack(Items.BIRCH_DOOR));
		register(getStack(Items.JUNGLE_DOOR));
		register(getStack(Items.OAK_DOOR));
		register(getStack(Items.SPRUCE_DOOR));
		register(getStack(Items.WOODEN_AXE));
		register(getStack(Items.WOODEN_HOE));
		register(getStack(Items.WOODEN_PICKAXE));
		register(getStack(Items.WOODEN_SHOVEL));
		register(getStack(Items.WOODEN_SWORD));
		register(getStack(Items.BED));
		register(getStack(Items.SKULL, 1, 0));
		register(getStack(Items.SKULL, 1, 2));
		register(getStack(Items.SKULL, 1, 4));
		for (int i = 0; i < StackWIPHandler.devHeads.size(); i++)
			register(StackWIPHandler.devHeads.get(i));
		register(getStack(Items.GLOWSTONE_DUST));
		register(getStack(Items.STRING));
		register(getStack(Items.MINECART));
		register(getStack(Items.CHEST_MINECART));
		register(getStack(Items.HOPPER_MINECART));
		register(getStack(Items.PRISMARINE_SHARD));
		register(getStack(Items.SHEARS));
		register(getStack(Items.EXPERIENCE_BOTTLE));
		register(getStack(Items.BONE));
		register(getStack(Items.BOWL));
		register(getStack(Items.BRICK));
		register(getStack(Items.FISHING_ROD));
		register(getStack(Items.BOOK));
		register(getStack(Items.PAPER));
		register(getStack(Items.SUGAR));
		register(getStack(Items.REEDS));
		register(getStack(Items.SPIDER_EYE));
		register(getStack(Items.SLIME_BALL));
		register(getStack(Items.ROTTEN_FLESH));
		register(getStack(Items.SIGN));
		register(getStack(Items.WRITABLE_BOOK));
		register(getStack(Items.COOKED_BEEF));
		register(getStack(Items.NAME_TAG));
		register(getStack(Items.SADDLE));
		register(getStack(Items.REDSTONE));
		register(getStack(Items.GUNPOWDER));
		register(getStack(Items.RABBIT_HIDE));
		register(getStack(Items.RABBIT_FOOT));
		register(getStack(Items.APPLE));
		register(getStack(Items.GOLDEN_APPLE));
		register(getStack(Items.GOLD_NUGGET));
		register(getStack(Items.SHULKER_SHELL));

		register(DynamicCell.getEmptyCell(1));
		register(getMaterial("water", Type.CELL));
		register(getMaterial("compressedair", Type.CELL));
		register(getMaterial("sap", Type.PART));
		register(getMaterial("rubber", Type.PART));

		register(getStack(Blocks.TRAPDOOR));
		register(getStack(Blocks.STONE_BUTTON));
		register(getStack(Blocks.WOODEN_BUTTON));
		register(getStack(Blocks.ACACIA_FENCE));
		register(getStack(Blocks.ACACIA_FENCE_GATE));
		register(getStack(Blocks.BIRCH_FENCE));
		register(getStack(Blocks.BIRCH_FENCE_GATE));
		register(getStack(Blocks.DARK_OAK_FENCE));
		register(getStack(Blocks.DARK_OAK_FENCE_GATE));
		register(getStack(Blocks.JUNGLE_FENCE));
		register(getStack(Blocks.JUNGLE_FENCE_GATE));
		register(getStack(Blocks.NETHER_BRICK_FENCE));
		register(getStack(Blocks.OAK_FENCE));
		register(getStack(Blocks.OAK_FENCE_GATE));
		register(getStack(Blocks.SPRUCE_FENCE));
		register(getStack(Blocks.SPRUCE_FENCE_GATE));
		register(getStack(Blocks.BRICK_BLOCK));
		register(getStack(Blocks.CRAFTING_TABLE));
		register(getStack(Blocks.PUMPKIN));
		register(getStack(Blocks.NETHERRACK));
		register(getStack(Blocks.GRASS));
		register(getStack(Blocks.DIRT, 1, 0));
		register(getStack(Blocks.DIRT, 1, 1));
		register(getStack(Blocks.SAND, 1, 0));
		register(getStack(Blocks.SAND, 1, 1));
		register(getStack(Blocks.GLOWSTONE));
		register(getStack(Blocks.GRAVEL));
		register(getStack(Blocks.HARDENED_CLAY));
		register(getStack(Blocks.GLASS));
		register(getStack(Blocks.GLASS_PANE));
		register(getStack(Blocks.CACTUS));
		register(getStack(Blocks.TALLGRASS, 1, 0));
		register(getStack(Blocks.TALLGRASS, 1, 1));
		register(getStack(Blocks.DEADBUSH));
		register(getStack(Blocks.CHEST));
		register(getStack(Blocks.TNT));
		register(getStack(Blocks.RAIL));
		register(getStack(Blocks.DETECTOR_RAIL));
		register(getStack(Blocks.GOLDEN_RAIL));
		register(getStack(Blocks.ACTIVATOR_RAIL));
		register(getStack(Blocks.YELLOW_FLOWER));
		register(getStack(Blocks.RED_FLOWER, 1, 0));
		register(getStack(Blocks.RED_FLOWER, 1, 1));
		register(getStack(Blocks.RED_FLOWER, 1, 2));
		register(getStack(Blocks.RED_FLOWER, 1, 3));
		register(getStack(Blocks.RED_FLOWER, 1, 4));
		register(getStack(Blocks.RED_FLOWER, 1, 5));
		register(getStack(Blocks.RED_FLOWER, 1, 6));
		register(getStack(Blocks.RED_FLOWER, 1, 7));
		register(getStack(Blocks.RED_FLOWER, 1, 8));
		register(getStack(Blocks.BROWN_MUSHROOM));
		register(getStack(Blocks.RED_MUSHROOM));
		register(getStack(Blocks.BROWN_MUSHROOM_BLOCK));
		register(getStack(Blocks.RED_MUSHROOM_BLOCK));
		register(getStack(Blocks.SAPLING, 1, 0));
		register(getStack(Blocks.SAPLING, 1, 1));
		register(getStack(Blocks.SAPLING, 1, 2));
		register(getStack(Blocks.SAPLING, 1, 3));
		register(getStack(Blocks.SAPLING, 1, 4));
		register(getStack(Blocks.SAPLING, 1, 5));
		register(getStack(Blocks.LEAVES, 1, 0));
		register(getStack(Blocks.LEAVES, 1, 1));
		register(getStack(Blocks.LEAVES, 1, 2));
		register(getStack(Blocks.LEAVES, 1, 3));
		register(getStack(Blocks.LEAVES2, 1, 0));
		register(getStack(Blocks.LEAVES2, 1, 1));

		register(getStack(ModBlocks.RUBBER_SAPLING));

		for (String i : ItemDusts.types) {
			if (!i.equals(ModItems.META_PLACEHOLDER)) {
				register(ItemDusts.getDustByName(i));
			}
		}

		for (String i : ItemNuggets.types) {
			if (!i.equals(ModItems.META_PLACEHOLDER)) {
				register(ItemNuggets.getNuggetByName(i));
			}
		}

		for (String i : ItemGems.types) {
			if (!i.equals(ModItems.META_PLACEHOLDER)) {
				register(ItemGems.getGemByName(i));
			}
		}

		registerDyable(Items.DYE);
		registerDyable(Blocks.WOOL);
		registerDyable(Blocks.CARPET);
		registerDyable(Blocks.STAINED_GLASS);
		registerDyable(Blocks.STAINED_GLASS_PANE);
		registerDyable(Blocks.STAINED_HARDENED_CLAY);

		for (int i = 0; i < ScrapboxList.stacks.size(); i++) {
			RecipeHandler.addRecipe(new ScrapboxRecipe(ScrapboxList.stacks.get(i)));
		}
	}

	static void register(ItemStack stack) {
		if(stack == null || stack.isEmpty()){
			return;
		}
		ScrapboxList.stacks.add(stack);
	}

	static void registerDyable(Item item) {
		for (int i = 0; i < 16; i++)
			register(getStack(item, 1, i));
	}

	static void registerDyable(Block block) {
		registerDyable(Item.getItemFromBlock(block));
	}
}
