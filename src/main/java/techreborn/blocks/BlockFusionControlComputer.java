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
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.fusionReactor.TileEntityFusionController;
import techreborn.utils.damageSources.FusionDamageSource;

public class BlockFusionControlComputer extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockFusionControlComputer(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.fusioncontrolcomputer");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		final TileEntityFusionController tileEntityFusionController = (TileEntityFusionController) world
				.getTileEntity(new BlockPos(x, y, z));
		tileEntityFusionController.checkCoils();
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.FUSION_CONTROLLER.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public void onEntityWalk(final World worldIn, final BlockPos pos, final Entity entityIn) {
		super.onEntityWalk(worldIn, pos, entityIn);
		if (worldIn.getTileEntity(pos) instanceof TileEntityFusionController) {
			if (((TileEntityFusionController) worldIn.getTileEntity(pos)).crafingTickTime != 0
					&& ((TileEntityFusionController) worldIn.getTileEntity(pos)).checkCoils()) {
				entityIn.attackEntityFrom(new FusionDamageSource(), 200F);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new TileEntityFusionController();
	}

	@Override
	public String getFront(final boolean isActive) {
		return this.prefix + "fusion_control_computer_front";
	}

	@Override
	public String getSide(final boolean isActive) {
		return this.prefix + "machine_side";
	}

	@Override
	public String getTop(final boolean isActive) {
		return this.prefix + "machine_side";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return this.prefix + "machine_side";
	}
}
