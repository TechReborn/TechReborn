package techreborn.api;

import net.minecraft.item.ItemStack;

public interface ISubItemRetriever {

    ItemStack getCellByName(String name);

    ItemStack getCellByName(String name, int count);

    ItemStack getCellByName(String name, int count, boolean lookForIC2);

    ItemStack getDustByName(String name);

    ItemStack getDustByName(String name, int count);

    ItemStack getSmallDustByName(String name);

    ItemStack getSmallDustByName(String name, int count);

    ItemStack getGemByName(String name);

    ItemStack getGemByName(String name, int count);

    ItemStack getIngotByName(String name);

    ItemStack getIngotByName(String name, int count);

    ItemStack getNuggetByName(String name);

    ItemStack getNuggetByName(String name, int count);

    ItemStack getPartByName(String name);

    ItemStack getPartByName(String name, int count);

    ItemStack getPlateByName(String name);

    ItemStack getPlateByName(String name, int count);

    ItemStack getUpgradeByName(String name);

    ItemStack getUpgradeByName(String name, int count);

    ItemStack getOreByName(String name);

    ItemStack getOreByName(String name, int count);

    ItemStack getStorageBlockByName(String name);

    ItemStack getStorageBlockByName(String name, int count);

}
