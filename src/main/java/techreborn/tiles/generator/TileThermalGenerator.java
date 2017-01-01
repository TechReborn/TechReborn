package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.util.FluidUtils;
import techreborn.api.generator.EFluidGenerator;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileThermalGenerator extends TileBaseFluidGenerator {

	public TileThermalGenerator() {
		super(EFluidGenerator.THERMAL, ConfigTechReborn.ThermalGeneratorTier, "TileThermalGenerator", 1000 * 10,
				ConfigTechReborn.ThermalGeneratorOutput);
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.THERMAL_GENERATOR, 1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!this.world.isRemote) {
			if (acceptFluid() && FluidUtils.drainContainers(this.tank, this.inventory, 0, 1))
				this.syncWithAll();
			for (final EnumFacing direction : EnumFacing.values()) {
				if (this.world
						.getBlockState(new BlockPos(this.getPos().getX() + direction.getFrontOffsetX(),
								this.getPos().getY() + direction.getFrontOffsetY(),
								this.getPos().getZ() + direction.getFrontOffsetZ()))
						.getBlock() == Blocks.LAVA) {
					if (this.tryAddingEnergy(1))
						this.lastOutput = this.world.getTotalWorldTime();
				}
			}
		}
	}

	@Override
	public double getMaxPower() {
		return ConfigTechReborn.ThermalGeneratorCharge;
	}

	@Override
	public double getMaxOutput() {
		return 128;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}
}
