package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileHeatGenerator;

public class BlockHeatGenerator extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockHeatGenerator(Material material) {
		super();
		setUnlocalizedName("techreborn.heatgenerator");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileHeatGenerator();
	}

	@Override
	public String getFront(boolean isActive) {
		return prefix + "heat_generator_side";
	}

	@Override
	public String getSide(boolean isActive) {
		return prefix + "heat_generator_side";
	}

	@Override
	public String getTop(boolean isActive) {
		return prefix + "heat_generator_top";
	}

	@Override
	public String getBottom(boolean isActive) {
		return prefix + "heat_generator_bottom";
	}

}
