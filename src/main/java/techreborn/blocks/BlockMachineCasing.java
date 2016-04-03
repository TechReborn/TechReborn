package techreborn.blocks;

import java.util.List;
import java.util.Random;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.multiblock.BlockMultiblockBase;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileMachineCasing;

public class BlockMachineCasing extends BlockMultiblockBase implements ITexturedBlock
{

	public static final String[] types = new String[] { "standard", "reinforced", "advanced" };
	public PropertyInteger METADATA;

	public BlockMachineCasing(Material material)
	{
		super(material);
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.machineCasing");
		setHardness(2F);
		this.setDefaultState(this.getDefaultState().withProperty(METADATA, 0));
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

		METADATA = PropertyInteger.create("type", 0, types.length);
		return new BlockStateContainer(this, METADATA);
	}

	public int getHeatFromState(IBlockState state)
	{
		switch (getMetaFromState(state))
		{
			case 0:
				return 1020 / 25;
			case 1:
				return 1700 / 25;
			case 2:
				return 2380 / 25;
		}
		return 0;
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
		int meta = getMetaFromState(state);
		return meta;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileMachineCasing();
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		Block b = worldIn.getBlockState(pos).getBlock();
		return b != (Block) this && super.shouldSideBeRendered(blockState, worldIn, pos, side);
	}

	public boolean shouldConnectToBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int meta)
	{
		return block == this;
	}

	@Override
	public String getTextureNameFromState(IBlockState blockState, EnumFacing facing)
	{
		return "techreborn:blocks/machine/machine_blocks/casing" + types[getMetaFromState(blockState)] + "_full";
	}

	@Override
	public int amountOfStates()
	{
		return types.length;
	}
}
