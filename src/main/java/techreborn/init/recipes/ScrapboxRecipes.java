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
		register(new ItemStack(Items.DIAMOND));
		register(new ItemStack(Items.STICK));
		register(new ItemStack(Items.COAL));
		register(new ItemStack(Items.APPLE));
		register(new ItemStack(Items.BAKED_POTATO));
		register(new ItemStack(Items.BLAZE_POWDER));
		register(new ItemStack(Items.WHEAT));
		register(new ItemStack(Items.CARROT));
		register(new ItemStack(Items.BOAT));
		register(new ItemStack(Items.ACACIA_BOAT));
		register(new ItemStack(Items.BIRCH_BOAT));
		register(new ItemStack(Items.DARK_OAK_BOAT));
		register(new ItemStack(Items.JUNGLE_BOAT));
		register(new ItemStack(Items.SPRUCE_BOAT));
		register(new ItemStack(Items.BLAZE_ROD));
		register(new ItemStack(Items.COMPASS));
		register(new ItemStack(Items.MAP));
		register(new ItemStack(Items.LEATHER_LEGGINGS));
		register(new ItemStack(Items.BOW));
		register(new ItemStack(Items.COOKED_CHICKEN));
		register(new ItemStack(Items.CAKE));
		register(new ItemStack(Items.ACACIA_DOOR));
		register(new ItemStack(Items.DARK_OAK_DOOR));
		register(new ItemStack(Items.BIRCH_DOOR));
		register(new ItemStack(Items.JUNGLE_DOOR));
		register(new ItemStack(Items.OAK_DOOR));
		register(new ItemStack(Items.SPRUCE_DOOR));
		register(new ItemStack(Items.WOODEN_AXE));
		register(new ItemStack(Items.WOODEN_HOE));
		register(new ItemStack(Items.WOODEN_PICKAXE));
		register(new ItemStack(Items.WOODEN_SHOVEL));
		register(new ItemStack(Items.WOODEN_SWORD));
		register(new ItemStack(Items.BED));
		register(new ItemStack(Items.SKULL, 1, 0));
		register(new ItemStack(Items.SKULL, 1, 2));
		register(new ItemStack(Items.SKULL, 1, 4));
		for (int i = 0; i < StackWIPHandler.devHeads.size(); i++)
			register(StackWIPHandler.devHeads.get(i));
		register(new ItemStack(Items.GLOWSTONE_DUST));
		register(new ItemStack(Items.STRING));
		register(new ItemStack(Items.MINECART));
		register(new ItemStack(Items.CHEST_MINECART));
		register(new ItemStack(Items.HOPPER_MINECART));
		register(new ItemStack(Items.PRISMARINE_SHARD));
		register(new ItemStack(Items.SHEARS));
		register(new ItemStack(Items.EXPERIENCE_BOTTLE));
		register(new ItemStack(Items.BONE));
		register(new ItemStack(Items.BOWL));
		register(new ItemStack(Items.BRICK));
		register(new ItemStack(Items.FISHING_ROD));
		register(new ItemStack(Items.BOOK));
		register(new ItemStack(Items.PAPER));
		register(new ItemStack(Items.SUGAR));
		register(new ItemStack(Items.REEDS));
		register(new ItemStack(Items.SPIDER_EYE));
		register(new ItemStack(Items.SLIME_BALL));
		register(new ItemStack(Items.ROTTEN_FLESH));
		register(new ItemStack(Items.SIGN));
		register(new ItemStack(Items.WRITABLE_BOOK));
		register(new ItemStack(Items.COOKED_BEEF));
		register(new ItemStack(Items.NAME_TAG));
		register(new ItemStack(Items.SADDLE));
		register(new ItemStack(Items.REDSTONE));
		register(new ItemStack(Items.GUNPOWDER));
		register(new ItemStack(Items.RABBIT_HIDE));
		register(new ItemStack(Items.RABBIT_FOOT));
		register(new ItemStack(Items.APPLE));
		register(new ItemStack(Items.GOLDEN_APPLE));
		register(new ItemStack(Items.GOLD_NUGGET));
		register(new ItemStack(Items.SHULKER_SHELL));

		register(DynamicCell.getEmptyCell(1));
		register(getMaterial("water", Type.CELL));
		register(getMaterial("compressedair", Type.CELL));
		register(getMaterial("sap", Type.PART));
		register(getMaterial("rubber", Type.PART));

		register(new ItemStack(Blocks.TRAPDOOR));
		register(new ItemStack(Blocks.STONE_BUTTON));
		register(new ItemStack(Blocks.WOODEN_BUTTON));
		register(new ItemStack(Blocks.ACACIA_FENCE));
		register(new ItemStack(Blocks.ACACIA_FENCE_GATE));
		register(new ItemStack(Blocks.BIRCH_FENCE));
		register(new ItemStack(Blocks.BIRCH_FENCE_GATE));
		register(new ItemStack(Blocks.DARK_OAK_FENCE));
		register(new ItemStack(Blocks.DARK_OAK_FENCE_GATE));
		register(new ItemStack(Blocks.JUNGLE_FENCE));
		register(new ItemStack(Blocks.JUNGLE_FENCE_GATE));
		register(new ItemStack(Blocks.NETHER_BRICK_FENCE));
		register(new ItemStack(Blocks.OAK_FENCE));
		register(new ItemStack(Blocks.OAK_FENCE_GATE));
		register(new ItemStack(Blocks.SPRUCE_FENCE));
		register(new ItemStack(Blocks.SPRUCE_FENCE_GATE));
		register(new ItemStack(Blocks.BRICK_BLOCK));
		register(new ItemStack(Blocks.CRAFTING_TABLE));
		register(new ItemStack(Blocks.PUMPKIN));
		register(new ItemStack(Blocks.NETHERRACK));
		register(new ItemStack(Blocks.GRASS));
		register(new ItemStack(Blocks.DIRT, 1, 0));
		register(new ItemStack(Blocks.DIRT, 1, 1));
		register(new ItemStack(Blocks.SAND, 1, 0));
		register(new ItemStack(Blocks.SAND, 1, 1));
		register(new ItemStack(Blocks.GLOWSTONE));
		register(new ItemStack(Blocks.GRAVEL));
		register(new ItemStack(Blocks.HARDENED_CLAY));
		register(new ItemStack(Blocks.GLASS));
		register(new ItemStack(Blocks.GLASS_PANE));
		register(new ItemStack(Blocks.CACTUS));
		register(new ItemStack(Blocks.TALLGRASS, 1, 0));
		register(new ItemStack(Blocks.TALLGRASS, 1, 1));
		register(new ItemStack(Blocks.DEADBUSH));
		register(new ItemStack(Blocks.CHEST));
		register(new ItemStack(Blocks.TNT));
		register(new ItemStack(Blocks.RAIL));
		register(new ItemStack(Blocks.DETECTOR_RAIL));
		register(new ItemStack(Blocks.GOLDEN_RAIL));
		register(new ItemStack(Blocks.ACTIVATOR_RAIL));
		register(new ItemStack(Blocks.YELLOW_FLOWER));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 0));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 1));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 2));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 3));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 4));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 5));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 6));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 7));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 8));
		register(new ItemStack(Blocks.BROWN_MUSHROOM));
		register(new ItemStack(Blocks.RED_MUSHROOM));
		register(new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK));
		register(new ItemStack(Blocks.RED_MUSHROOM_BLOCK));
		register(new ItemStack(Blocks.SAPLING, 1, 0));
		register(new ItemStack(Blocks.SAPLING, 1, 1));
		register(new ItemStack(Blocks.SAPLING, 1, 2));
		register(new ItemStack(Blocks.SAPLING, 1, 3));
		register(new ItemStack(Blocks.SAPLING, 1, 4));
		register(new ItemStack(Blocks.SAPLING, 1, 5));
		register(new ItemStack(Blocks.LEAVES, 1, 0));
		register(new ItemStack(Blocks.LEAVES, 1, 1));
		register(new ItemStack(Blocks.LEAVES, 1, 2));
		register(new ItemStack(Blocks.LEAVES, 1, 3));
		register(new ItemStack(Blocks.LEAVES2, 1, 0));
		register(new ItemStack(Blocks.LEAVES2, 1, 1));

		register(new ItemStack(ModBlocks.RUBBER_SAPLING));

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
		ScrapboxList.stacks.add(stack);
	}

	static void registerDyable(Item item) {
		for (int i = 0; i < 16; i++)
			register(new ItemStack(item, 1, i));
	}

	static void registerDyable(Block block) {
		registerDyable(Item.getItemFromBlock(block));
	}
}
