package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileRollingMachine;

public class BlockRollingMachine extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockRollingMachine(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.rollingmachine");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileRollingMachine();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.ROLLING_MACHINE.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public String getFront(final boolean isActive) {
		return isActive ? this.prefix + "rolling_machine_side_on" : this.prefix + "rolling_machine_side_off";
	}

	@Override
	public String getSide(final boolean isActive) {
		return this.prefix + "machine_side";
	}

	@Override
	public String getTop(final boolean isActive) {
		return this.prefix + "machine_top";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return this.prefix + "machine_bottom";
	}
}
