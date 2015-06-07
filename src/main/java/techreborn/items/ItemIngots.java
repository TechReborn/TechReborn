package techreborn.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;

public class ItemIngots extends Item {	
	public static ItemStack getIngotByName(String name, int count)
	{
		int index = -1;
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(name)) {
				index = i;
				break;
			}
		}
		return new ItemStack(ModItems.ingots, count, index);
	}
	
	public static ItemStack getIngotByName(String name)
	{
		return getIngotByName(name, 1);
	}
	
	public static final String[] types = new String[]
	{ "aluminum", "antimony", "batteryAlloy", "redAlloy", "blueAlloy", "brass",
			"bronze", "cadmium", "chrome", "copper", "cupronickel", "electrum", "indium",
			"invar", "iridium", "kanthal", "lead", "lodestone", "magnalium", "nichrome", "nickel",
			"osmium", "platinum", "silver", "steel", "tellurium", "tin", "titanium",
			"tungsten", "hotTungstensteel", "tungstensteel", "zinc" };

	private IIcon[] textures;

	public ItemIngots()
	{
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHasSubtypes(true);
		setUnlocalizedName("techreborn.ingot");
	}

	@Override
	// Registers Textures For All Dusts
	public void registerIcons(IIconRegister iconRegister)
	{
		textures = new IIcon[types.length];

		for (int i = 0; i < types.length; ++i)
		{
			textures[i] = iconRegister.registerIcon("techreborn:" + "ingot/"
					+ types[i] + "Ingot");
		}
	}

	@Override
	// Adds Texture what match's meta data
	public IIcon getIconFromDamage(int meta)
	{
		if (meta < 0 || meta >= textures.length)
		{
			meta = 0;
		}

		return textures[meta];
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

}
