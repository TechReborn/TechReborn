package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

/**
 * Created by mark on 19/02/2016.
 */
public class BlockRubberLog extends Block implements ITexturedBlock {
	public BlockRubberLog() {
		super(Material.wood);
		setUnlocalizedName("techreborn.rubberlog");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override
	public String getTextureNameFromState(IBlockState iBlockState, EnumFacing enumFacing) {
		if(enumFacing == EnumFacing.DOWN || enumFacing == EnumFacing.UP){
			return  "techreborn:blocks/rubber_log_side";
		}
		return "techreborn:blocks/rubber_log";
	}

	@Override
	public int amountOfStates() {
		return 1;
	}

	@Override
	public boolean canSustainLeaves(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isWood(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return true;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		int i = 4;
		int j = i + 1;

		if (worldIn.isAreaLoaded(pos.add(-j, -j, -j), pos.add(j, j, j))) {
			for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-i, -i, -i), pos.add(i, i, i))) {
				IBlockState iblockstate = worldIn.getBlockState(blockpos);

				if (iblockstate.getBlock().isLeaves(worldIn, blockpos)) {
					iblockstate.getBlock().beginLeavesDecay(worldIn, blockpos);
				}
			}
		}
	}
}
