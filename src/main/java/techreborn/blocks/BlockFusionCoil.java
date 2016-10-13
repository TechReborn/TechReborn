package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.TechRebornCreativeTab;

public class BlockFusionCoil extends BlockMachineBase {

	public BlockFusionCoil(Material material) {
		super(true);
		setUnlocalizedName("techreborn.fusioncoil");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[0]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}
}
