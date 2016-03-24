package techreborn.blocks.advanced_machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileIndustrialGrinder;

public class BlockIndustrialGrinder extends BlockMachineBase implements IRotationTexture
{

	public BlockIndustrialGrinder(Material material)
	{
		super();
		setUnlocalizedName("techreborn.industrialgrinder");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileIndustrialGrinder();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
			float hitY, float hitZ)
	{
		if (fillBlockWithFluid(world, new BlockPos(x, y, z), player))
		{
			return true;
		}
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.industrialGrinderID, world, x, y, z);
		return true;
	}

	private final String prefix = "techreborn:blocks/machine/advanced_machines/";

	@Override
	public String getFrontOff()
	{
		return prefix + "industrial_grinder_front_off";
	}

	@Override
	public String getFrontOn()
	{
		return prefix + "industrial_grinder_front_on";
	}

	@Override
	public String getSide()
	{
		return prefix + "machine_side";
	}

	@Override
	public String getTop()
	{
		return prefix + "industrial_grinder_top_off";
	}

	@Override
	public String getBottom()
	{
		return prefix + "industrial_centrifuge_bottom";
	}
}
