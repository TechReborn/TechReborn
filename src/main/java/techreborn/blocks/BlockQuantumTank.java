package techreborn.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileQuantumTank;

public class BlockQuantumTank extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockQuantumTank() {
		super();
		this.setUnlocalizedName("techreborn.quantumTank");
		this.setHardness(2.0F);
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileQuantumTank();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (this.fillBlockWithFluid(world, new BlockPos(x, y, z), player)) {
			return true;
		}
		if (!player.isSneaking()) {
			player.openGui(Core.INSTANCE, EGui.QUANTUM_TANK.ordinal(), world, x, y, z);
		}
		return true;
	}

	@Override
	public String getFront(final boolean isActive) {
		return "techreborn:blocks/machine/generators/thermal_generator_side_off";
	}

	@Override
	public String getSide(final boolean isActive) {
		return "techreborn:blocks/machine/generators/thermal_generator_side_off";
	}

	@Override
	public String getTop(final boolean isActive) {
		return this.prefix + "quantum_top";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return "techreborn:blocks/machine/generators/thermal_generator_bottom";
	}
}
