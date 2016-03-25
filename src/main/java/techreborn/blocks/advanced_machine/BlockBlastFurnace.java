package techreborn.blocks.advanced_machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileBlastFurnace;

public class BlockBlastFurnace extends BlockMachineBase implements IRotationTexture
{

	private final String prefix = "techreborn:blocks/machine/advanced_machines/";

	public BlockBlastFurnace(Material material)
	{
		super();
		setUnlocalizedName("techreborn.blastfurnace");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileBlastFurnace();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
			float hitY, float hitZ)
	{
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.blastFurnaceID, world, x, y, z);
		return true;
	}

	@Override
	public boolean isAdvanced()
	{
		return true;
	}

	@Override
	public String getFrontOff()
	{
		return prefix + "industrial_blast_furnace_front_off";
	}

	@Override
	public String getFrontOn()
	{
		return prefix + "industrial_blast_furnace_front_on";
	}

	@Override
	public String getSide()
	{
		return prefix + "advanced_machine_side";
	}

	@Override
	public String getTop()
	{
		return prefix + "advanced_machine_top";
	}

	@Override
	public String getBottom()
	{
		return prefix + "advanced_machine_bottom";
	}
}
