package techreborn.blocks.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileThermalGenerator;

public class BlockThermalGenerator extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockThermalGenerator() {
		super();
		setUnlocalizedName("techreborn.thermalGenerator");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileThermalGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
	                                float hitY, float hitZ) {
		if (fillBlockWithFluid(world, new BlockPos(x, y, z), player)) {
			return true;
		}
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.thermalGeneratorID, world, x, y, z);
		return true;
	}

	@Override
	public String getFront(boolean isActive) {
		return isActive ? prefix + "thermal_generator_side_on" : prefix + "thermal_generator_side_off";
	}

	@Override
	public String getSide(boolean isActive) {
		return isActive ? prefix + "thermal_generator_side_on" : prefix + "thermal_generator_side_off";
	}

	@Override
	public String getTop(boolean isActive) {
		return isActive ? prefix + "thermal_generator_top_on" : prefix + "thermal_generator_top_off";
	}

	@Override
	public String getBottom(boolean isActive) {
		return prefix + "generator_machine_bottom";
	}
}
