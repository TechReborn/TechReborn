package techreborn.blocks;

import java.security.InvalidParameterException;
import java.util.List;

import com.google.common.collect.Lists;
import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.BaseBlock;
import reborncore.common.blocks.PropertyString;
import reborncore.common.util.ArrayUtils;
import reborncore.common.util.StringUtils;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModBlocks;
import techreborn.world.config.IOreNameProvider;

public class BlockOre2 extends BaseBlock implements IOreNameProvider
{

	public static final String[] ores = new String[] { "copper", "tin"};
	static List<String> oreNamesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(ores));
	public PropertyString VARIANTS = new PropertyString("type", oreNamesList);

	public BlockOre2(Material material)
	{
		super(material);
		setUnlocalizedName("techreborn.ore2");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(2.0f);
		setHarvestLevel("pickaxe", 1);
		this.setDefaultState(this.getStateFromMeta(0));
	}

	public static ItemStack getOreByName(String name, int count)
	{
		for (int i = 0; i < ores.length; i++)
		{
			if (ores[i].equalsIgnoreCase(name))
			{
				return new ItemStack(ModBlocks.ore2, count, i);
			}
		}
		throw new InvalidParameterException("The ore block " + name + " could not be found.");
	}

	public static ItemStack getOreByName(String name)
	{
		return getOreByName(name, 1);
	}

	public IBlockState getBlockStateFromName(String name)
	{
		int index = -1;
		for (int i = 0; i < ores.length; i++)
		{
			if (ores[i].equalsIgnoreCase(name))
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

	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
	{
		for (int meta = 0; meta < ores.length; meta++)
		{
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
								  EntityPlayer player)
	{
		return new ItemStack(this,1, getMetaFromState(state));
	}

//	@Override
//	public int damageDropped(IBlockState state)
//	{
//		int meta = getMetaFromState(state);
//		if (meta == 2)
//		{
//			return 0;
//		} else if (meta == 3)
//		{
//			return 1;
//		} else if (meta == 5)
//		{
//			return 60;
//		}
//		return meta;
//	}

//	@Override
//	public String getTextureNameFromState(IBlockState BlockStateContainer, EnumFacing facing)
//	{
//		return "techreborn:blocks/ore/ore" + StringUtils.toFirstCapital(ores[getMetaFromState(BlockStateContainer)]);
//	}
//
//	@Override
//	public int amountOfStates()
//	{
//		return ores.length;
//	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if(meta > ores.length){
			meta = 0;
		}
		return getBlockState().getBaseState().withProperty(VARIANTS, oreNamesList.get(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return oreNamesList.indexOf(state.getValue(VARIANTS));
	}

	protected BlockStateContainer createBlockState()
	{
		VARIANTS = new PropertyString("type", oreNamesList);
		return new BlockStateContainer(this, VARIANTS);
	}

	@Override
	public String getUserLoclisedName(IBlockState state)
	{
		return StringUtils.toFirstCapital(oreNamesList.get(getMetaFromState(state)));
	}
}
