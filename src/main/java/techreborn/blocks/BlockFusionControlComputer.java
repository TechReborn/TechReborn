package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.fusionReactor.TileEntityFusionController;
import techreborn.utils.damageSources.FusionDamageSource;

public class BlockFusionControlComputer extends BlockMachineBase implements IAdvancedRotationTexture
{

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockFusionControlComputer(Material material)
	{
		super();
		setUnlocalizedName("techreborn.fusioncontrolcomputer");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
			float hitY, float hitZ)
	{
		TileEntityFusionController tileEntityFusionController = (TileEntityFusionController) world
				.getTileEntity(new BlockPos(x, y, z));
		tileEntityFusionController.checkCoils();
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.fusionID, world, x, y, z);
		return true;
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		super.onEntityWalk(worldIn, pos, entityIn);
		if (worldIn.getTileEntity(pos) instanceof TileEntityFusionController)
		{
			if (((TileEntityFusionController) worldIn.getTileEntity(pos)).crafingTickTime != 0
					&& ((TileEntityFusionController) worldIn.getTileEntity(pos)).checkCoils())
			{
				entityIn.attackEntityFrom(new FusionDamageSource(), 200F);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityFusionController();
	}

	@Override
	public String getFront(boolean isActive)
	{
		return prefix + "fusion_control_computer_front";
	}

	@Override
	public String getSide(boolean isActive)
	{
		return prefix + "machine_side";
	}

	@Override
	public String getTop(boolean isActive)
	{
		return prefix + "machine_side";
	}

	@Override
	public String getBottom(boolean isActive)
	{
		return prefix + "machine_side";
	}
}
