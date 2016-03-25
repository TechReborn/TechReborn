package techreborn.items;

import java.security.InvalidParameterException;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;

public class ItemPurifiedCrushedOre extends ItemTextureBase
{

	public static final String[] types = new String[] { "Aluminum", "Ardite", "Bauxite", "Cadmium", "Cinnabar",
			"Cobalt", "DarkIron", "Indium", "Iridium", "Nickel", "Osmium", "Platinum", "Pyrite", "Sphalerite",
			"Tetrahedrite", "Tungsten", "Galena" };

	public ItemPurifiedCrushedOre()
	{
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHasSubtypes(true);
		setUnlocalizedName("techreborn.purifiedcrushedore");
	}

	public static ItemStack getPurifiedCrushedOreByName(String name, int count)
	{
		for (int i = 0; i < types.length; i++)
		{
			if (types[i].equalsIgnoreCase(name))
			{
				return new ItemStack(ModItems.purifiedCrushedOre, count, i);
			}
		}
		throw new InvalidParameterException("The crushed ore " + name + " could not be found.");
	}

	public static ItemStack getPurifiedCrushedOreByName(String name)
	{
		return getPurifiedCrushedOreByName(name, 1);
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(ItemStack itemStack)
	{
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length)
		{
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
	{
		for (int meta = 0; meta < types.length; ++meta)
		{
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public String getTextureName(int damage)
	{
		return ModInfo.MOD_ID + ":items/purifiedCrushedOre/purifiedCrushed" + types[damage] + "Ore";
	}

	@Override
	public int getMaxMeta()
	{
		return types.length;
	}

}
