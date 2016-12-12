package techreborn.init;

import net.minecraft.item.ItemStack;
import techreborn.api.ISubItemRetriever;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockStorage;
import techreborn.items.*;

/**
 * Created by Mark on 03/04/2016.
 */
public class SubItemRetriever implements ISubItemRetriever {
	@Override
	public ItemStack getCellByName(String name) {
		return ItemCells.getCellByName(name);
	}

	@Override
	public ItemStack getCellByName(String name, int count) {
		return ItemCells.getCellByName(name, count);
	}

	@Override
	public ItemStack getCellByName(String name, int count, boolean lookForIC2) {
		return ItemCells.getCellByName(name, count);
	}

	@Override
	public ItemStack getDustByName(String name) {
		return ItemDusts.getDustByName(name);
	}

	@Override
	public ItemStack getDustByName(String name, int count) {
		return ItemDusts.getDustByName(name, count);
	}

	@Override
	public ItemStack getSmallDustByName(String name) {
		return ItemDustsSmall.getSmallDustByName(name);
	}

	@Override
	public ItemStack getSmallDustByName(String name, int count) {
		return ItemDustsSmall.getSmallDustByName(name, count);
	}

	@Override
	public ItemStack getGemByName(String name) {
		return ItemGems.getGemByName(name);
	}

	@Override
	public ItemStack getGemByName(String name, int count) {
		return ItemGems.getGemByName(name, count);
	}

	@Override
	public ItemStack getIngotByName(String name) {
		return ItemIngots.getIngotByName(name);
	}

	@Override
	public ItemStack getIngotByName(String name, int count) {
		return ItemIngots.getIngotByName(name, count);
	}

	@Override
	public ItemStack getNuggetByName(String name) {
		return ItemNuggets.getNuggetByName(name);
	}

	@Override
	public ItemStack getNuggetByName(String name, int count) {
		return ItemNuggets.getNuggetByName(name, count);
	}

	@Override
	public ItemStack getPartByName(String name) {
		return ItemParts.getPartByName(name);
	}

	@Override
	public ItemStack getPartByName(String name, int count) {
		return ItemParts.getPartByName(name, count);
	}

	@Override
	public ItemStack getPlateByName(String name) {
		return ItemPlates.getPlateByName(name);
	}

	@Override
	public ItemStack getPlateByName(String name, int count) {
		return ItemPlates.getPlateByName(name, count);
	}

	@Override
	public ItemStack getUpgradeByName(String name) {
		return ItemUpgrades.getUpgradeByName(name);
	}

	@Override
	public ItemStack getUpgradeByName(String name, int count) {
		return ItemUpgrades.getUpgradeByName(name, count);
	}

	@Override
	public ItemStack getOreByName(String name) {
		return BlockOre.getOreByName(name);
	}

	@Override
	public ItemStack getOreByName(String name, int count) {
		return BlockOre.getOreByName(name, count);
	}

	@Override
	public ItemStack getStorageBlockByName(String name) {
		return BlockStorage.getStorageBlockByName(name);
	}

	@Override
	public ItemStack getStorageBlockByName(String name, int count) {
		return BlockStorage.getStorageBlockByName(name, count);
	}
}
