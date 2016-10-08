package techreborn.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;

import java.security.InvalidParameterException;
import java.util.List;

public class ItemDusts extends ItemTextureBase {
	public static final String[] types = new String[] { "almandine", "aluminum", "andradite", "ashes", "basalt",
		"bauxite", "brass", "bronze", "calcite", "charcoal", "chrome", "cinnabar", "clay", "coal", "copper",
		"darkAshes", "diamond", "electrum", "emerald", "enderEye", "enderPearl", "endstone", "flint", "galena",
		"gold", "grossular", "invar", "iron", "lazurite", "lead", "magnesium", "manganese", "marble", "netherrack",
		"nickel", "obsidian", "peridot", "phosphorous", "platinum", "pyrite", "pyrope", "redGarnet", ModItems.META_PLACEHOLDER,
		"ruby", "saltpeter", "sapphire", "sawDust", "silver", "sodalite", "spessartine", "sphalerite", "steel",
		"sulfur", "tin", "titanium", "tungsten", "uvarovite", ModItems.META_PLACEHOLDER, "yellowGarnet", "zinc",
		"olivine", "andesite", "diorite", "granite" };

	public ItemDusts() {
		setUnlocalizedName("techreborn.dust");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
	}

	public static ItemStack getDustByName(String name, int count) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				if (types[i].equals(ModItems.META_PLACEHOLDER)) {
					throw new InvalidParameterException("The dust " + name + " could not be found.");
				}
				return new ItemStack(ModItems.dusts, count, i);
			}
		}

		if (name.equalsIgnoreCase("glowstone")) {
			return new ItemStack(Items.GLOWSTONE_DUST, count);
		}
		if (name.equalsIgnoreCase("redstone")) {
			return new ItemStack(Items.REDSTONE, count);
		}
		if (name.equalsIgnoreCase("gunpowder")) {
			return new ItemStack(Items.GUNPOWDER, count);
		}
		throw new InvalidParameterException("The dust " + name + " could not be found.");
	}

	public static ItemStack getDustByName(String name) {
		return getDustByName(name, 1);
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
			if (!types[meta].equals(ModItems.META_PLACEHOLDER)) {
				list.add(new ItemStack(item, 1, meta));
			}
		}
	}

	@Override
	public String getTextureName(int damage) {
		if (types[damage].equals("%NULL%")) {
			damage = 0;
		}
		return ModInfo.MOD_ID + ":items/dust/" + types[damage] + "Dust";
	}

	@Override
	public int getMaxMeta() {
		return types.length;
	}

}
