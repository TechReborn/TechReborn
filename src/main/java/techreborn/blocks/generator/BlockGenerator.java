package techreborn.blocks.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileGenerator;

public class BlockGenerator extends BlockMachineBase implements IRotationTexture {

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockGenerator() {
		super();
		this.setUnlocalizedName("techreborn.generator");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileGenerator();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking()) {
			player.openGui(Core.INSTANCE, EGui.GENERATOR.ordinal(), world, x, y, z);
		}
		return true;
	}

	@Override
	public String getFrontOff() {
		return this.prefix + "generator_front_off";
	}

	@Override
	public String getFrontOn() {
		return this.prefix + "generator_front_on";
	}

	@Override
	public String getSide() {
		return this.prefix + "generator_machine_side";
	}

	@Override
	public String getTop() {
		return this.prefix + "generator_machine_top";
	}

	@Override
	public String getBottom() {
		return this.prefix + "generator_machine_bottom";
	}
}
