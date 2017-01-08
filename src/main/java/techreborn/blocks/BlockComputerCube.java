package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;

public class BlockComputerCube extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockComputerCube(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.computercube");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.MANUAL.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public String getFront(final boolean isActive) {
		return this.prefix + "computer_cube";
	}

	@Override
	public String getSide(final boolean isActive) {
		return this.prefix + "computer_cube";
	}

	@Override
	public String getTop(final boolean isActive) {
		return this.prefix + "computer_cube";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return this.prefix + "computer_cube";
	}

}