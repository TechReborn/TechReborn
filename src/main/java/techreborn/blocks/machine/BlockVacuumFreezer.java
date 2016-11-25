package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.multiblock.TileVacuumFreezer;

public class BlockVacuumFreezer extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockVacuumFreezer(Material material) {
		super();
		setUnlocalizedName("techreborn.vacuumfreezer");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileVacuumFreezer();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			player.openGui(Core.INSTANCE, GuiHandler.vacuumFreezerID, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public String getFront(boolean isActive) {
		return prefix + "vacuum_freezer_front";
	}

	@Override
	public String getSide(boolean isActive) {
		return prefix + "machine_side";
	}

	@Override
	public String getTop(boolean isActive) {
		return prefix + "vacuum_freezer_top";
	}

	@Override
	public String getBottom(boolean isActive) {
		return prefix + "machine_bottom";
	}
}
