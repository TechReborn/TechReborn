package techreborn.items;

import com.google.common.base.CaseFormat;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;

import java.security.InvalidParameterException;

public class ItemPlates extends ItemTRNoDestroy {

	//Vanilla plates or plates not from ingots or gems
	public static String[] types = new String[] {
		"iron", "gold", "carbon", "wood", "redstone", "diamond", "emerald", ModItems.META_PLACEHOLDER, "coal", "obsidian", "lazurite"
	};

	public ItemPlates() {
		setUnlocalizedName("techreborn.plate");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
	}

	public static ItemStack getPlateByName(String name, int count) {
		name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				if (types[i].equals(ModItems.META_PLACEHOLDER)) {
					throw new InvalidParameterException("The dust " + name + " could not be found.");
				}
				return new ItemStack(ModItems.plate, count, i);
			}
		}
		throw new InvalidParameterException("The plate " + name + " could not be found.");
	}

	public static ItemStack getPlateByName(String name) {
		return getPlateByName(name, 1);
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
		String oreName = "plate" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, plateType);
		OreDictionary.registerOre(oreName, new ItemStack(ModItems.plate, 1, plateIndex));
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
	public void getSubItems(Item item, CreativeTabs creativeTabs, NonNullList list) {
		for (int meta = 0; meta < types.length; ++meta) {
			if (!types[meta].equals(ModItems.META_PLACEHOLDER)) {
				list.add(new ItemStack(item, 1, meta));
			}
		}
	}
}
