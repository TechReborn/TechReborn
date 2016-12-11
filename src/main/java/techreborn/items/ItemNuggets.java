package techreborn.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;

import java.security.InvalidParameterException;

public class ItemNuggets extends ItemTRNoDestroy {

	public static final String[] types = new String[] { "aluminum", "brass", "bronze", "chrome", "copper", "electrum",
		"invar", "iridium", "lead", "nickel", "platinum", "silver", "steel", "tin", "titanium", "tungsten",
		"hot_tungstensteel", "tungstensteel", "zinc", "refined_iron", ModItems.META_PLACEHOLDER, ModItems.META_PLACEHOLDER,
		ModItems.META_PLACEHOLDER, "iron", "diamond" };

	public ItemNuggets() {
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHasSubtypes(true);
		setUnlocalizedName("techreborn.nuggets");
	}

	public static ItemStack getNuggetByName(String name, int count) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.nuggets, count, i);
			}
		}
		throw new InvalidParameterException("The nugget " + name + " could not be found.");
	}

	public static ItemStack getNuggetByName(String name) {
		return getNuggetByName(name, 1);
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
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, NonNullList list) {
		for (int meta = 0; meta < types.length; ++meta) {
			if (!types[meta].equals(ModItems.META_PLACEHOLDER)) {
				list.add(new ItemStack(item, 1, meta));
			}
		}
	}

}
