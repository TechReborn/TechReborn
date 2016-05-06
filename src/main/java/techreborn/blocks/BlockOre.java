package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.BaseBlock;
import reborncore.common.util.OreDrop;
import reborncore.common.util.OreDropSet;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.items.ItemDusts;
import techreborn.items.ItemGems;
import techreborn.world.config.IOreNameProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockOre extends BaseBlock implements ITexturedBlock, IOreNameProvider
{

	public static final String[] types = new String[] { "Galena", "Iridium", "Ruby", "Sapphire", "Bauxite", "Pyrite",
			"Cinnabar", "Sphalerite", "Tungston", "Sheldonite", "Peridot", "Sodalite",
			"Lead", "Silver" };
	public PropertyInteger METADATA;

	public BlockOre(Material material)
	{
		super(material);
		setUnlocalizedName("techreborn.ore");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(2.0f);
		setHarvestLevel("pickaxe", 2);
		this.setDefaultState(this.getDefaultState().withProperty(METADATA, 0));
	}

	public static ItemStack getOreByName(String name, int count)
	{
		for (int i = 0; i < types.length; i++)
		{
			if (types[i].equalsIgnoreCase(name))
			{
				return new ItemStack(ModBlocks.ore, count, i);
			}
		}
		return BlockOre2.getOreByName(name, count);
	}

	public static ItemStack getOreByName(String name)
	{
		return getOreByName(name, 1);
	}

	public IBlockState getBlockStateFromName(String name)
	{
		int index = -1;
		for (int i = 0; i < types.length; i++)
		{
			if (types[i].equalsIgnoreCase(name))
			{
				index = i;
				break;
			}
		}
		if (index == -1)
		{
			return ModBlocks.ore2.getBlockStateFromName(name);
		}
		return getStateFromMeta(index);
	}

	@Deprecated
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		int metadata = getMetaFromState(state);
		Random random = new Random();
		// Ruby
		if (metadata == 2)
		{
			OreDrop ruby = new OreDrop(ItemGems.getGemByName("ruby"),
					ConfigTechReborn.FortuneSecondaryOreMultiplierPerLevel);
			OreDrop redGarnet = new OreDrop(ItemGems.getGemByName("redGarnet"), 0.02);
			OreDropSet set = new OreDropSet(ruby, redGarnet);
			return set.drop(fortune, random);
		}

		// Sapphire
		if (metadata == 3)
		{
			OreDrop sapphire = new OreDrop(ItemGems.getGemByName("sapphire"),
					ConfigTechReborn.FortuneSecondaryOreMultiplierPerLevel);
			OreDrop peridot = new OreDrop(ItemGems.getGemByName("peridot"), 0.03);
			OreDropSet set = new OreDropSet(sapphire, peridot);
			return set.drop(fortune, random);
		}

		// Pyrite
		if (metadata == 5)
		{
			OreDrop pyriteDust = new OreDrop(ItemDusts.getDustByName("pyrite"),
					ConfigTechReborn.FortuneSecondaryOreMultiplierPerLevel);
			OreDropSet set = new OreDropSet(pyriteDust);
			return set.drop(fortune, random);
		}

		// Sodalite
		if (metadata == 11)
		{
			OreDrop sodalite = new OreDrop(ItemDusts.getDustByName("sodalite", 6),
					ConfigTechReborn.FortuneSecondaryOreMultiplierPerLevel);
			OreDrop aluminum = new OreDrop(ItemDusts.getDustByName("aluminum"), 0.50);
			OreDropSet set = new OreDropSet(sodalite, aluminum);
			return set.drop(fortune, random);
		}

		// Cinnabar
		if (metadata == 6)
		{
			OreDrop cinnabar = new OreDrop(ItemDusts.getDustByName("cinnabar"),
					ConfigTechReborn.FortuneSecondaryOreMultiplierPerLevel);
			OreDrop redstone = new OreDrop(new ItemStack(Items.REDSTONE), 0.25);
			OreDropSet set = new OreDropSet(cinnabar, redstone);
			return set.drop(fortune, random);
		}

		// Sphalerite 1, 1/8 yellow garnet
		if (metadata == 7)
		{
			OreDrop sphalerite = new OreDrop(ItemDusts.getDustByName("sphalerite"),
					ConfigTechReborn.FortuneSecondaryOreMultiplierPerLevel);
			OreDrop yellowGarnet = new OreDrop(ItemGems.getGemByName("yellowGarnet"), 0.125);
			OreDropSet set = new OreDropSet(sphalerite, yellowGarnet);
			return set.drop(fortune, random);
		}

		ArrayList<ItemStack> block = new ArrayList<>();
		block.add(new ItemStack(Item.getItemFromBlock(this), 1, metadata));
		return block;
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
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player)
	{
		return super.getPickBlock(state, target, world, pos, player);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		int meta = getMetaFromState(state);
		if (meta == 2)
		{
			return 0;
		} else if (meta == 3)
		{
			return 1;
		} else if (meta == 5)
		{
			return 60;
		}
		return meta;
	}

	@Override
	public String getTextureNameFromState(IBlockState BlockStateContainer, EnumFacing facing)
	{
		return "techreborn:blocks/ore/ore" + types[getMetaFromState(BlockStateContainer)];
	}

	@Override
	public int amountOfStates()
	{
		return types.length;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(METADATA, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(METADATA);
	}

	protected BlockStateContainer createBlockState()
	{

		METADATA = PropertyInteger.create("type", 0, types.length - 1);
		return new BlockStateContainer(this, METADATA);
	}

	@Override
	public String getUserLoclisedName(IBlockState state)
	{
		return types[state.getValue(METADATA)];
	}
}
