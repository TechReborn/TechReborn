package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.TechRebornCreativeTab;

public class BlockFusionCoil extends BlockMachineBase {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockFusionCoil(Material material) {
		super();
		setUnlocalizedName("techreborn.fusioncoil");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public String getTextureNameFromState(IBlockState BlockStateContainer, EnumFacing facing) {
		return prefix + "fusion_coil";
	}
}
