package techreborn.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemDusts extends ItemTR {
	public static final String[] types = new String[]
	{ 		"almandine", "aluminumBrass", "aluminum", "alumite", "andradite",
			"antimony", "ardite", "ashes", "basalt", "bauxite", "biotite",
			"brass", "bronze", "cadmium", "calcite", "charcoal", "chrome", 
			"cinnabar", "clay", "coal", "cobalt", "copper", "cupronickel", 
			"darkAshes", "darkIron", "diamond", "electrum", "emerald",
			"enderEye", "enderPearl", "endstone", "flint", "gold", "graphite",
			"grossular", "indium", "invar", "iridium", "iron", "kanthal", "lapis", "lazurite",
			"lead", "limestone", "lodestone", "magnesium", "magnetite", "manganese", 
			"manyullyn", "marble", "mithril", "netherrack", "nichrome", "nickel",
			"obsidian", "osmium", "peridot", "phosphorous", "platinum", "potassiumFeldspar", 
			"pyrite", "pyrope", "redGarnet", "redrock", "ruby", "saltpeter",
			"sapphire", "silicon", "silver", "sodalite", "spessartine", "sphalerite",
			"steel", "sulfur", "tellurium", "teslatite", "tetrahedrite", "tin",
			"titanium", "tungsten", "uvarovite", "vinteum", "voidstone", "yellowGarnet",
			"zinc", "galena" };

	private IIcon[] textures;

	public ItemDusts()
	{
		setUnlocalizedName("techreborn.dust");
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
			textures[i] = iconRegister.registerIcon("techreborn:" + "dust/"
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

}
