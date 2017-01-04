package techreborn.init.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.util.OreUtil;
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
		} else {

		}
		return ItemStack.EMPTY;
	}

	static ItemStack getMaterial(String name, Type type) {
		return getMaterial(name, 1, type);
	}

	static ItemStack getOre(String name, int count) {
		return OreUtil.getStackFromName(name, count);
	}

	static ItemStack getOre(String name) {
		return getOre(name, 1);
	}

	static boolean oresExist(String... names) {
		for (String name : names) {
			if (!OreDictionary.doesOreNameExist(name)) {
				return false;
			}
		}
		return true;
	}

	enum Type {
		DUST, SMALL_DUST, INGOT, NUGGET, PLATE, GEM, CELL, PART, CABLE
	}
}
