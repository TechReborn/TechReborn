package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.BaseBlock;
import techreborn.client.TechRebornCreativeTabMisc;

public class BlockReinforcedGlass extends BaseBlock implements ITexturedBlock {

	private final String prefix = "techreborn:blocks/";

	public BlockReinforcedGlass(Material materialIn) {
		super(materialIn);
		setUnlocalizedName("techreborn.reinforcedglass");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(4.0F);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	public boolean isFullCube() {
		return false;
	}

	@Override
	public int amountOfStates() {
		return 1;
	}

	@Override
	public String getTextureNameFromState(IBlockState arg0, EnumFacing arg1) {
		return prefix + "reinforcedglass";
	}

}
