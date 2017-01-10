package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileSemifluidGenerator;

public class BlockSemiFluidGenerator extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockSemiFluidGenerator(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.semifluidgenerator");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileSemifluidGenerator();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (this.fillBlockWithFluid(world, new BlockPos(x, y, z), player)) {
			return true;
		}
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.SEMIFLUID_GENERATOR.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public String getFront(final boolean isActive) {
		return this.prefix + "semifluid_generator_side";
	}

	@Override
	public String getSide(final boolean isActive) {
		return this.prefix + "semifluid_generator_side";
	}

	@Override
	public String getTop(final boolean isActive) {
		return this.prefix + "generator_machine_top";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return this.prefix + "generator_machine_bottom";
	}

}
