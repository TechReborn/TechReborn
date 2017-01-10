package techreborn.blocks.advanced_machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileCentrifuge;

public class BlockCentrifuge extends BlockMachineBase implements IRotationTexture {

	private final String prefix = "techreborn:blocks/machine/advanced_machines/";

	public BlockCentrifuge() {
		super();
		this.setUnlocalizedName("techreborn.centrifuge");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileCentrifuge();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking()) {
			player.openGui(Core.INSTANCE, EGui.CENTRIFUGE.ordinal(), world, x, y, z);
		}
		return true;
	}

	@Override
	public String getFrontOff() {
		return this.prefix + "industrial_centrifuge_side_off";
	}

	@Override
	public String getFrontOn() {
		return this.prefix + "industrial_centrifuge_side_on";
	}

	@Override
	public String getSide() {
		return this.getFrontOff();
	}

	@Override
	public String getTop() {
		return this.prefix + "industrial_centrifuge_top_off";
	}

	@Override
	public String getBottom() {
		return this.prefix + "industrial_centrifuge_bottom";
	}
}
