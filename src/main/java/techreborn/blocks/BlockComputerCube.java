package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;

public class BlockComputerCube extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockComputerCube(Material material) {
		super();
		setUnlocalizedName("techreborn.computercube");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
	                                float hitY, float hitZ) {
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.manuelID, world, x, y, z);
		return true;
	}

	@Override
	public String getFront(boolean isActive) {
		return prefix + "computer_cube";
	}

	@Override
	public String getSide(boolean isActive) {
		return prefix + "computer_cube";
	}

	@Override
	public String getTop(boolean isActive) {
		return prefix + "computer_cube";
	}

	@Override
	public String getBottom(boolean isActive) {
		return prefix + "computer_cube";
	}

}