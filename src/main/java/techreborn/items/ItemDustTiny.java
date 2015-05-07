package techreborn.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemDustTiny extends ItemTR {
	public static final String[] types = new String[]
	{ 		"Almandine", "AluminumBrass", "Aluminum", "Alumite", "Andradite", 
			"Antimony", "Ardite", "Ashes", "Basalt", "Bauxite", "Biotite",
			"Brass", "Bronze", "Cadmium", "Calcite", "Charcoal", "Chrome", 
			"Cinnabar", "Clay", "Coal", "Cobalt", "Copper", "Cupronickel", 
			"DarkAshes", "DarkIron", "Diamond", "Electrum", "Emerald",
			"EnderEye", "EnderPearl", "Endstone", "Flint", "Glowstone", "Gold", "Graphite",
			"Grossular", "Gunpowder", "Indium", "Invar", "Iridium", "Iron", "Kanthal", "Lapis", "Lazurite",
			"Lead", "Limestone", "Lodestone", "Magnesium", "Magnetite", "Manganese", 
			"Manyullyn", "Marble", "Mithril", "Netherrack", "Nichrome", "Nickel",
			"Obsidian", "Osmium", "Peridot", "Phosphorous", "Platinum", "PotassiumFeldspar", 
			"Pyrite", "Pyrope", "RedGarnet", "Redrock", "Redstone", "Ruby", "Saltpeter",
			"Sapphire", "Silicon", "Silver", "Sodalite", "Spessartine", "Sphalerite",
			"Steel", "Sulfur", "Tellurium", "Teslatite", "Tetrahedrite", "Tin",
			"Titanium", "Tungsten", "Uvarovite", "Vinteum", "Voidstone", "YellowGarnet",
			"Zinc", "Galena" };

	private IIcon[] textures;

	public ItemDustTiny()
	{
		setUnlocalizedName("techreborn.dusttiny");
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
			textures[i] = iconRegister.registerIcon("techreborn:" + "tinyDust/tiny"
					+ types[i] + "Dust");
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
		return EnumRarity.uncommon;
	}

}
