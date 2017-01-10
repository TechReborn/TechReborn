package techreborn.blocks.tier1;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.teir1.TileGrinder;

public class BlockGrinder extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/tier1_machines/";

	public BlockGrinder(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.grinder");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileGrinder();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking()) {
			player.openGui(Core.INSTANCE, EGui.GRINDER.ordinal(), world, x, y, z);
		}
		return true;
	}

	@Override
	public String getFront(final boolean isActive) {
		return isActive ? this.prefix + "grinder_front_on" : this.prefix + "grinder_front_off";
	}

	@Override
	public String getSide(final boolean isActive) {
		return this.prefix + "tier1_machine_side";
	}

	@Override
	public String getTop(final boolean isActive) {
		return isActive ? this.prefix + "grinder_top_on" : this.prefix + "grinder_top_off";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return this.prefix + "tier1_machine_bottom";
	}

}
