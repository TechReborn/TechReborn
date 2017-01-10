package techreborn.blocks.tier1;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.teir1.TileElectricFurnace;

public class BlockElectricFurnace extends BlockMachineBase implements IRotationTexture {

	private final String prefix = "techreborn:blocks/machine/tier1_machines/";

	public BlockElectricFurnace(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.electricfurnace");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileElectricFurnace();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking()) {
			player.openGui(Core.INSTANCE, EGui.ELECTRIC_FURNACE.ordinal(), world, x, y, z);
		}
		return true;
	}

	@Override
	public String getFrontOff() {
		return this.prefix + "electric_furnace_front_off";
	}

	@Override
	public String getFrontOn() {
		return this.prefix + "electric_furnace_front_on";
	}

	@Override
	public String getSide() {
		return this.prefix + "tier1_machine_side";
	}

	@Override
	public String getTop() {
		return this.prefix + "tier1_machine_top";
	}

	@Override
	public String getBottom() {
		return this.prefix + "tier1_machine_bottom";
	}
}
