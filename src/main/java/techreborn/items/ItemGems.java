package techreborn.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;

import java.security.InvalidParameterException;
import java.util.List;

public class ItemGems extends ItemTextureBase {

	public static final String[] types = new String[] { "ruby", "sapphire", "peridot", "redGarnet", "yellowGarnet" };

	public ItemGems() {
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setUnlocalizedName("techreborn.gem");
		setHasSubtypes(true);
	}

	public static ItemStack getGemByName(String name, int count) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.gems, count, i);
			}
		}
		throw new InvalidParameterException("The gem " + name + " could not be found.");
	}

	public static ItemStack getGemByName(String name) {
		return getGemByName(name, 1);
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

	@Override
	public String getTextureName(int damage) {
		return ModInfo.MOD_ID + ":items/gem/" + types[damage];
	}

	@Override
	public int getMaxMeta() {
		return types.length;
	}

}
