package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.api.power.EnumPowerTier;
import techreborn.api.generator.EFluidGenerator;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileGasTurbine extends TileBaseFluidGenerator {

	public TileGasTurbine() {
		super(EFluidGenerator.GAS, ConfigTechReborn.ThermalGeneratorTier, "TileGasTurbine", 1000 * 10, 16);
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.gasTurbine, 1);
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.MEDIUM;
	}

	@Override
	public double getMaxPower() {
		return ConfigTechReborn.ThermalGeneratorCharge;
	}
}
