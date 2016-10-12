package techreborn.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;
import techreborn.utils.OreDictUtils;

import java.security.InvalidParameterException;
import java.util.List;

public class ItemPlates extends ItemTextureBase {

	//Vanilla plates or plates not from ingots or gems
	public static String[] types = new String[] {
		"iron", "gold", "carbon", "wood", "redstone", "diamond", "emerald", "lapis", "coal", "obsidian", "lazurite", "advancedAlloy"
	};

	public ItemPlates() {
		setUnlocalizedName("techreborn.plate");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
	}

	public static ItemStack getPlateByName(String name, int count) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.plate, count, i);
			}
		}
		throw new InvalidParameterException("The plate " + name + " could not be found.");
	}

	public static ItemStack getPlateByName(String name) {
		return getPlateByName(name, 1);
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
		for (int meta = 0; meta < types.length; ++meta) {
			list.add(new ItemStack(item, 1, meta));
		}
	}

	public static void registerType(String plateType) {
		for (String type : types) {
			if (type.equals(plateType))
				return;
		}
		int plateIndex = types.length;
		String[] newTypes = new String[plateIndex + 1];
		System.arraycopy(types, 0, newTypes, 0, types.length);
		types = newTypes;
		newTypes[plateIndex] = plateType;
		String oreName = "plate" + OreDictUtils.toFirstUpper(plateType);
		OreDictionary.registerOre(oreName, new ItemStack(ModItems.plate, 1, plateIndex));
	}

	@Override
	public String getTextureName(int damage) {
		return ModInfo.MOD_ID + ":items/plate/" + types[damage] + "Plate";
	}

	@Override
	public int getMaxMeta() {
		return types.length;
	}

}
