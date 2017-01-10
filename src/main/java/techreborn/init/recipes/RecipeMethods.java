package techreborn.init.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.util.OreUtil;
import techreborn.blocks.BlockMachineCasing;
import techreborn.blocks.BlockMachineFrame;
import techreborn.init.IC2Duplicates;
import techreborn.items.*;
import techreborn.parts.powerCables.ItemStandaloneCables;

/**
 * Created by Prospector
 */
public abstract class RecipeMethods {
	static ItemStack getMaterial(String name, int count, Type type) {
		if (type == Type.DUST) {
			return ItemDusts.getDustByName(name, count);
		} else if (type == Type.SMALL_DUST) {
			return ItemDustsSmall.getSmallDustByName(name, count);
		} else if (type == Type.INGOT) {
			return ItemIngots.getIngotByName(name, count);
		} else if (type == Type.GEM) {
			return ItemGems.getGemByName(name, count);
		} else if (type == Type.PLATE) {
			return ItemPlates.getPlateByName(name, count);
		} else if (type == Type.NUGGET) {
			return ItemNuggets.getNuggetByName(name, count);
		} else if (type == Type.CELL) {
			return ItemCells.getCellByName(name, count);
		} else if (type == Type.PART) {
			return ItemParts.getPartByName(name, count);
		} else if (type == Type.CABLE) {
			return ItemStandaloneCables.getCableByName(name, count);
		} else if (type == Type.MACHINE_FRAME) {
			return BlockMachineFrame.getFrameByName(name, count);
		} else if (type == Type.MACHINE_CASING) {
			return BlockMachineCasing.getStackByName(name, count);
		} else if (type == Type.UPGRADE) {
			return ItemUpgrades.getUpgradeByName(name, count);
		} else {

		}
		return null;
	}

	static ItemStack getMaterial(String name, Type type) {
		return getMaterial(name, 1, type);
	}

	static ItemStack getOre(String name, int count) {
		return OreUtil.getStackFromName(name, count).copy();
	}

	static ItemStack getOre(String name) {
		return getOre(name, 1);
	}

	static boolean oresExist(String... names) {
		for (String name : names) {
			if (OreDictionary.getOres(name).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	static ItemStack getStack(Item item) {
		return getStack(item, 1);
	}

	static ItemStack getStack(Item item, int count) {
		return getStack(item, count, 0);
	}

	static ItemStack getStack(Item item, boolean wildcard) {
		return getStack(item, 1, true);
	}

	static ItemStack getStack(Item item, int count, boolean wildcard) {
		return getStack(item, count, OreDictionary.WILDCARD_VALUE);
	}

	static ItemStack getStack(Item item, int count, int metadata) {
		return new ItemStack(item, count, metadata);
	}

	static ItemStack getStack(Block block) {
		return getStack(block, 1);
	}

	static ItemStack getStack(Block block, int count) {
		return getStack(block, count, 0);
	}

	static ItemStack getStack(Block block, boolean wildcard) {
		return getStack(block, 1, true);
	}

	static ItemStack getStack(Block block, int count, boolean wildcard) {
		return getStack(block, count, OreDictionary.WILDCARD_VALUE);
	}

	static ItemStack getStack(Block block, int count, int metadata) {
		return getStack(Item.getItemFromBlock(block), count, OreDictionary.WILDCARD_VALUE);
	}

	static ItemStack getStack(IC2Duplicates ic2Duplicates) {
		return getStack(ic2Duplicates, 1);
	}

	static ItemStack getStack(IC2Duplicates ic2Duplicates, int count) {
		ItemStack stack = ic2Duplicates.getStackBasedOnConfig();
		stack.stackSize = (count);
		return stack;
	}

	enum Type {
		DUST, SMALL_DUST, INGOT, NUGGET, PLATE, GEM, CELL, PART, CABLE, MACHINE_FRAME, MACHINE_CASING, UPGRADE
	}
}
