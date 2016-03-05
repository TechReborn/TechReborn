package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import reborncore.common.BaseBlock;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.TechRebornCreativeTabMisc;

public class BlockReinforcedGlass extends BaseBlock implements ITexturedBlock {
	
	public BlockReinforcedGlass(Material materialIn) {
		super(materialIn);
		setUnlocalizedName("techreborn.reinforcedglass");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(4.0F);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
    private final String prefix = "techreborn:blocks/machine/";

	@Override
	public int amountOfStates() {
		return 1;
	}

	@Override
	public String getTextureNameFromState(IBlockState arg0, EnumFacing arg1) {
		return prefix + "reinforcedglass";
	}
	
}
