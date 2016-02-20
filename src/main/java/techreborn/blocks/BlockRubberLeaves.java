package techreborn.blocks;

import biomesoplenty.common.enums.BOPTrees;
import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

import java.util.List;

/**
 * Created by mark on 20/02/2016.
 */
public class BlockRubberLeaves extends BlockLeaves implements ITexturedBlock {

	public BlockRubberLeaves() {
		super();
		setUnlocalizedName("techreborn.rubberleaves");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		RebornCore.jsonDestroyer.registerObject(this);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
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
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return Blocks.leaves.getBlockLayer();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return Blocks.leaves.isOpaqueCube();
	}

	@Override
	protected ItemStack createStackedBlock(IBlockState state)
	{
		IBlockState newState = state.withProperty(CHECK_DECAY, Boolean.valueOf(false)).withProperty(DECAYABLE, Boolean.valueOf(false));

		return super.createStackedBlock(newState);
	}

	@Override
	public String getTextureNameFromState(IBlockState iBlockState, EnumFacing enumFacing) {
		return "techreborn:blocks/rubber_leaves";
	}

	@Override
	public int amountOfStates() {
		return 4;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { CHECK_DECAY, DECAYABLE});
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
}
