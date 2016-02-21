package techreborn.blocks;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.world.RubberTreeGenerator;
import techreborn.world.TreeGenerator;

import java.util.List;
import java.util.Random;

/**
 * Created by Mark on 20/02/2016.
 */
public class BlockRubberSapling extends BlockSapling {

    public BlockRubberSapling() {
        setUnlocalizedName("techreborn.rubbersapling");
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, Integer.valueOf(0)));
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) {
            return;
        }
        worldIn.setBlockToAir(pos);
        new RubberTreeGenerator(false).generate(worldIn, rand, pos);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
    }
}
