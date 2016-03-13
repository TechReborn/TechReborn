package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.IOpaqueBlock;
import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModBlocks;

import java.util.List;
import java.util.Random;

/**
 * Created by modmuss50 on 20/02/2016.
 */
public class BlockRubberLeaves extends BlockLeaves implements ITexturedBlock, IOpaqueBlock {

	public BlockRubberLeaves() {
		super();
		setUnlocalizedName("techreborn.rubberleaves");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		RebornCore.jsonDestroyer.registerObject(this);
		this.setDefaultState(this.getDefaultState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta) {
		return null;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		List<ItemStack> list = new java.util.ArrayList<ItemStack>();
		list.add(new ItemStack(this, 1, 0));
		return list;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return this.leavesFancy ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return Blocks.leaves.isOpaqueCube(state);
	}

	public boolean isFullCube()
	{
		return false;
	}



	@Override
	protected ItemStack createStackedBlock(IBlockState state)
	{
		IBlockState newState = state.withProperty(CHECK_DECAY, Boolean.valueOf(false)).withProperty(DECAYABLE, Boolean.valueOf(false));

		return super.createStackedBlock(newState);
	}

	@Override
	public String getTextureNameFromState(IBlockState IBlockState, EnumFacing enumFacing) {
		return "techreborn:blocks/rubber_leaves";
	}

	@Override
	public int amountOfStates() {
		return 4;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { CHECK_DECAY, DECAYABLE});
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DECAYABLE, Boolean.valueOf((meta & 1) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 2) > 0));
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta = 0;
		if (!((Boolean)state.getValue(DECAYABLE)).booleanValue())
		{
			meta |= 1;
		}
		if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue())
		{
			meta |= 2;
		}
		return meta;
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		return 16777215;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		return 16777215;
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		return 16777215;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.rubberSapling);
	}
}
