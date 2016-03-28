package techreborn.blocks.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileGenerator;

public class BlockGenerator extends BlockMachineBase implements IRotationTexture
{

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockGenerator()
	{
		super();
		setUnlocalizedName("techreborn.generator");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
			float hitY, float hitZ)
	{
		if (!player.isSneaking())
		{
			player.openGui(Core.INSTANCE, GuiHandler.generatorID, world, x, y, z);
		}
		return true;
	}

	@Override
	public String getFrontOff()
	{
		return prefix + "generator_front_off";
	}

	@Override
	public String getFrontOn()
	{
		return prefix + "generator_front_on";
	}

	@Override
	public String getSide()
	{
		return prefix + "generator_machine_side";
	}

	@Override
	public String getTop()
	{
		return prefix + "generator_machine_top";
	}

	@Override
	public String getBottom()
	{
		return prefix + "generator_machine_bottom";
	}
}
