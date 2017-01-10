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
import techreborn.tiles.teir1.TileExtractor;

public class BlockExtractor extends BlockMachineBase implements IRotationTexture {

	private final String prefix = "techreborn:blocks/machine/tier1_machines/";

	public BlockExtractor(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.extractor");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileExtractor();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking()) {
			player.openGui(Core.INSTANCE, EGui.EXTRACTOR.ordinal(), world, x, y, z);
		}
		return true;
	}

	@Override
	public String getFrontOff() {
		return this.prefix + "extractor_front_off";
	}

	@Override
	public String getFrontOn() {
		return this.prefix + "extractor_front_on";
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
