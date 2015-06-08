package techreborn.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.pahimar.ee3.item.ItemGem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import techreborn.init.ModBlocks;
import techreborn.items.ItemDusts;
import techreborn.items.ItemGems;

public class BlockOre extends Block {

	public static ItemStack getOreByName(String name, int count)
	{
		int index = -1;
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(name)) {
				index = i;
				break;
			}
		}
		return new ItemStack(ModBlocks.ore, count, index);
	}
	
	public static ItemStack getOreByName(String name)
	{
		return getOreByName(name, 1);
	}

	public static final String[] types = new String[]
	{ "Galena", "Iridium", "Ruby", "Sapphire", "Bauxite", "Pyrite", "Cinnabar",
			"Sphalerite", "Tungston", "Sheldonite", "Peridot", "Sodalite",
			"Tetrahedrite", "Cassiterite", "Lead", "Silver" };

	private IIcon[] textures;

	public BlockOre(Material material)
	{
		super(material);
		setBlockName("techreborn.ore");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(1f);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		//Ruby
		if (metadata == 2)
		{
			OreDrop ruby = new OreDrop(ItemGems.getGemByName("ruby"));
			OreDrop redGarnet = new OreDrop(ItemGems.getGemByName("redGarnet"), 0.02);
			OreDropSet set = new OreDropSet(ruby, redGarnet);
			return set.drop(fortune, world.rand);
		}

		//Sapphire
		if (metadata == 3)
		{
			OreDrop sapphire = new OreDrop(ItemGems.getGemByName("sapphire"));
			OreDrop peridot = new OreDrop(ItemGems.getGemByName("peridot"), 0.03);
			OreDropSet set = new OreDropSet(sapphire, peridot);
			return set.drop(fortune, world.rand);
		}

		//Pyrite
		if (metadata == 5)
		{
			OreDrop pyriteDust = new OreDrop(ItemDusts.getDustByName("pyrite"));
			OreDropSet set = new OreDropSet(pyriteDust);
			return set.drop(fortune, world.rand);
		}

		//Sodolite
		if (metadata == 11)
		{
			OreDrop sodalite = new OreDrop(ItemDusts.getDustByName("sodalite", 6));
			OreDrop aluminum = new OreDrop(ItemDusts.getDustByName("aluminum"), 0.50);
			OreDropSet set = new OreDropSet(sodalite, aluminum);
			return set.drop(fortune, world.rand);
		}

		//Cinnabar
		if (metadata == 6)
		{
			OreDrop cinnabar = new OreDrop(ItemDusts.getDustByName("cinnabar"));
			OreDrop redstone = new OreDrop(new ItemStack(Items.redstone), 0.25);
			OreDropSet set = new OreDropSet(cinnabar, redstone);
			return set.drop(fortune, world.rand);
		}

		//Sphalerite 1, 1/8 yellow garnet
		if (metadata == 7)
		{
			OreDrop sphalerite = new OreDrop(ItemDusts.getDustByName("sphalerite"));
			OreDrop yellowGarnet = new OreDrop(ItemGems.getGemByName("yellowGarnet"), 0.125);
			OreDropSet set = new OreDropSet(sphalerite, yellowGarnet);
			return set.drop(fortune, world.rand);
		}

		ArrayList<ItemStack> block = new ArrayList<ItemStack>();
		block.add(new ItemStack(Item.getItemFromBlock(this)));
		return  block;
	}
		
    @Override
    protected boolean canSilkHarvest() 
    {
        return true;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
	{
		for (int meta = 0; meta < types.length; meta++)
		{
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public int damageDropped(int metaData)
	{
		if(metaData == 2 )
		{
			return 0;
		}
		else if(metaData == 3)
		{
			return 1;
		}
		else if(metaData == 5)
		{
			return 60;
		}
		return metaData;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.textures = new IIcon[types.length];

		for (int i = 0; i < types.length; i++)
		{
			textures[i] = iconRegister.registerIcon("techreborn:" + "ore/ore"
					+ types[i]);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metaData)
	{
		metaData = MathHelper.clamp_int(metaData, 0, types.length - 1);

		if (ForgeDirection.getOrientation(side) == ForgeDirection.UP
				|| ForgeDirection.getOrientation(side) == ForgeDirection.DOWN)
		{
			return textures[metaData];
		} 
		else
		{
			return textures[metaData];
		}
	}
}
