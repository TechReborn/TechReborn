package techreborn.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemDustsSmall extends ItemTR {
	public static final String[] types = new String[]
	{ "Almandine", "Aluminium", "Andradite", "Basalt", "Bauxite", "Brass",
			"Bronze", "Calcite", "Charcoal", "Chrome", "Cinnabar", "Clay",
			"Coal", "Copper", "Diamond", "Electrum", "Emerald", "EnderEye",
			"EnderPearl", "Endstone", "Gold", "GreenSapphire", "Grossular",
			"Invar", "Iron", "Lazurite", "Lead", "Magnesium", "Marble",
			"Netherrack", "Nickel", "Obsidian", "Olivine", "Platinum",
			"Pyrite", "Pyrope", "RedGarnet", "Ruby", "Saltpeter", "Sapphire",
			"Silver", "Sodalite", "Steel", "Sulfur", "Tin", "Titanium",
			"Tungsten", "Zinc", };

	private IIcon[] textures;

	public ItemDustsSmall()
	{
		setUnlocalizedName("techreborn.dustsmall");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
	}

	@Override
	// Registers Textures For All Dusts
	public void registerIcons(IIconRegister iconRegister)
	{
		textures = new IIcon[types.length];

		for (int i = 0; i < types.length; ++i)
		{
			textures[i] = iconRegister.registerIcon("techreborn:"
					+ "smallDust/small" + types[i] + "Dust");
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

	@Override
	public EnumRarity getRarity(ItemStack itemstack)
	{
		return EnumRarity.epic;
	}

}
