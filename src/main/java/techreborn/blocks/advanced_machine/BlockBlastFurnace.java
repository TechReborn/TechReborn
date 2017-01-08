package techreborn.blocks.advanced_machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.multiblock.TileBlastFurnace;

public class BlockBlastFurnace extends BlockMachineBase implements IRotationTexture {

	private final String prefix = "techreborn:blocks/machine/advanced_machines/";

	public BlockBlastFurnace(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.blastfurnace");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileBlastFurnace();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.BLAST_FURNACE.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public boolean isAdvanced() {
		return true;
	}

	@Override
	public String getFrontOff() {
		return this.prefix + "industrial_blast_furnace_front_off";
	}

	@Override
	public String getFrontOn() {
		return this.prefix + "industrial_blast_furnace_front_on";
	}

	@Override
	public String getSide() {
		return this.prefix + "advanced_machine_side";
	}

	@Override
	public String getTop() {
		return this.prefix + "advanced_machine_top";
	}

	@Override
	public String getBottom() {
		return this.prefix + "advanced_machine_bottom";
	}
}
