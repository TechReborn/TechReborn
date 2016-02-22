package techreborn.blocks;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTabMisc;

public class BlockIronFenceGate extends BlockFenceGate {

	public BlockIronFenceGate() {
		super(BlockPlanks.EnumType.OAK);
        setUnlocalizedName("techreborn.ironfencegate");
        setCreativeTab(TechRebornCreativeTabMisc.instance);
		setStepSound(soundTypeMetal);
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 2);
	}
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		return true;
    }
	
	@Override
	public Material getMaterial(){
		return Material.iron;
	}

}
