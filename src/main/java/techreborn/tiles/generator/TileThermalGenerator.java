package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import reborncore.api.power.EnumPowerTier;
import reborncore.common.util.FluidUtils;

import techreborn.api.generator.EFluidGenerator;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileThermalGenerator extends TileBaseFluidGenerator implements IContainerProvider {

	public TileThermalGenerator() {
		super(EFluidGenerator.THERMAL, ConfigTechReborn.ThermalGeneratorTier, "TileThermalGenerator", 1000 * 10,
				ConfigTechReborn.ThermalGeneratorOutput);
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.THERMAL_GENERATOR, 1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!this.world.isRemote) {
			if (this.acceptFluid() && FluidUtils.drainContainers(this.tank, this.inventory, 0, 1))
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

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("thermalgenerator").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(this).slot(0, 80, 17).outputSlot(1, 80, 53).fakeSlot(2, 59, 42).syncEnergyValue()
				.addInventory().create();
	}
}
