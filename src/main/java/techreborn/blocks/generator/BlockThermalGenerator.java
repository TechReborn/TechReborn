package techreborn.blocks.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileThermalGenerator;

public class BlockThermalGenerator extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockThermalGenerator() {
		super();
		this.setUnlocalizedName("techreborn.thermalGenerator");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileThermalGenerator();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (this.fillBlockWithFluid(world, new BlockPos(x, y, z), player)) {
			return true;
		}
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.THERMAL_GENERATOR.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public String getFront(final boolean isActive) {
		return isActive ? this.prefix + "thermal_generator_side_on" : this.prefix + "thermal_generator_side_off";
	}

	@Override
	public String getSide(final boolean isActive) {
		return isActive ? this.prefix + "thermal_generator_side_on" : this.prefix + "thermal_generator_side_off";
	}

	@Override
	public String getTop(final boolean isActive) {
		return isActive ? this.prefix + "thermal_generator_top_on" : this.prefix + "thermal_generator_top_off";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return this.prefix + "generator_machine_bottom";
	}
}
