package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import reborncore.api.power.EnumPowerTier;

import techreborn.api.generator.EFluidGenerator;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileSemifluidGenerator extends TileBaseFluidGenerator implements IContainerProvider {

	public TileSemifluidGenerator() {
		super(EFluidGenerator.SEMIFLUID, ConfigTechReborn.ThermalGeneratorTier, "TileSemifluidGenerator", 1000 * 10, 8);
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer arg0) {
		return new ItemStack(ModBlocks.SEMIFLUID_GENERATOR, 1);
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

	@Override
	public double getMaxPower() {
		return ConfigTechReborn.ThermalGeneratorCharge;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("semifluidgenerator").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(this).slot(0, 80, 17).outputSlot(1, 80, 53).fakeSlot(2, 59, 42).syncEnergyValue()
				.addInventory().create();
	}
}
