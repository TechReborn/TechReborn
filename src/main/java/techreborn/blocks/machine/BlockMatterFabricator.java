package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileMatterFabricator;

public class BlockMatterFabricator extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockMatterFabricator(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.matterfabricator");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileMatterFabricator();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.MATTER_FABRICATOR.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public boolean isAdvanced() {
		return true;
	}

	@Override
	public String getFront(final boolean isActive) {
		return isActive ? this.prefix + "matter_fabricator_on" : this.prefix + "matter_fabricator_off";
	}

	@Override
	public String getSide(final boolean isActive) {
		return isActive ? this.prefix + "matter_fabricator_on" : this.prefix + "matter_fabricator_off";
	}

	@Override
	public String getTop(final boolean isActive) {
		return isActive ? this.prefix + "matter_fabricator_on" : this.prefix + "matter_fabricator_off";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return isActive ? this.prefix + "matter_fabricator_on" : this.prefix + "matter_fabricator_off";
	}
}
