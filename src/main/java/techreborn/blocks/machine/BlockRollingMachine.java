package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileRollingMachine;

public class BlockRollingMachine extends BlockMachineBase implements IAdvancedRotationTexture
{

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockRollingMachine(Material material)
	{
		super();
		setUnlocalizedName("techreborn.rollingmachine");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileRollingMachine();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
			float hitY, float hitZ)
	{
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.rollingMachineID, world, x, y, z);
		return true;
	}

	@Override
	public String getFront(boolean isActive)
	{
		return isActive ? prefix + "rolling_machine_side_on" : prefix + "rolling_machine_side_off";
	}

	@Override
	public String getSide(boolean isActive)
	{
		return prefix + "machine_side";
	}

	@Override
	public String getTop(boolean isActive)
	{
		return prefix + "machine_top";
	}

	@Override
	public String getBottom(boolean isActive)
	{
		return prefix + "machine_bottom";
	}
}
