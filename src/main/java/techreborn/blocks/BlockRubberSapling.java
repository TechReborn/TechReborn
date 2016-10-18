package techreborn.blocks;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.world.RubberTreeGenerator;

import java.util.List;
import java.util.Random;

/**
 * Created by modmuss50 on 20/02/2016.
 */
public class BlockRubberSapling extends BlockSapling {

	public BlockRubberSapling() {
		setUnlocalizedName("techreborn.rubbersapling");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.setDefaultState(this.getDefaultState().withProperty(STAGE, 0));
		setSoundType(SoundType.PLANT);
	}

	@Override
	public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) {
			return;
		}
		worldIn.setBlockToAir(pos);
		if (!new RubberTreeGenerator(false).growTree(worldIn, rand, pos.getX(), pos.getY(), pos.getZ())) {
			worldIn.setBlockState(pos, state); // Re-add the sapling if the tree
			// failed to grow
		}
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(itemIn, 1, 0));
	}
}
