package techreborn.blocks.generator;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TilePlasmaGenerator;

public class BlockPlasmaGenerator extends BlockMachineBase implements ITexturedBlock {

	private final String prefix = "techreborn:blocks/machines/generators/";

	public BlockPlasmaGenerator(Material material) {
		super();
		setUnlocalizedName("techreborn.plasmagenerator");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public String getTextureNameFromState(IBlockState state, EnumFacing side){
		if(side == EnumFacing.UP){
			return prefix + "plasmagenerator_top_on";
		} else if (side == EnumFacing.DOWN){
			return prefix + "plasmagenerator_bottom";
		}
		return prefix + "plasmagenerator_side_on";
	}

	@Override
	public int amountOfStates(){
		return 9;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TilePlasmaGenerator();
	}
}
