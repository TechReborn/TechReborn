package techreborn.blocks;

import com.google.common.collect.Lists;
import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.BaseBlock;
import reborncore.common.blocks.PropertyString;
import reborncore.common.util.ArrayUtils;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModBlocks;

import java.util.List;
import java.util.Random;

public class BlockStorage extends BaseBlock
{

	public static final String[] types = new String[] { "silver", "aluminum", "titanium", "chrome", "steel", "brass",
			"lead", "electrum", "zinc", "platinum", "tungsten", "nickel", "invar", "iridium" };
	static List<String> oreNamesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(types));
	public PropertyString VARIANTS = new PropertyString("type", oreNamesList);

	public BlockStorage(Material material)
	{
		super(material);
		setUnlocalizedName("techreborn.storage");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(2f);
		this.setDefaultState(this.getStateFromMeta(0));
	}

	public static ItemStack getStorageBlockByName(String name, int count)
	{
		for (int i = 0; i < types.length; i++)
		{
			if (types[i].equals(name))
			{
				return new ItemStack(ModBlocks.storage, count, i);
			}
		}
		return BlockStorage2.getStorageBlockByName(name, count);
	}

	public static ItemStack getStorageBlockByName(String name)
	{
		return getStorageBlockByName(name, 1);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
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
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if(meta > types.length){
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

}
