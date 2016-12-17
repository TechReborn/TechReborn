package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.api.power.EnumPowerTier;
import techreborn.api.generator.EFluidGenerator;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileDieselGenerator extends TileBaseFluidGenerator {

	public TileDieselGenerator() {
		super(EFluidGenerator.DIESEL, ConfigTechReborn.ThermalGeneratorTier, "TileDieselGenerator", 1000*10, ConfigTechReborn.ThermalGeneratorOutput);
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.dieselGenerator, 1);
	}

	@Override
	public double getMaxPower() {
		return ConfigTechReborn.ThermalGeneratorCharge;
	}

	@Override
	public double getMaxOutput() {
		return 64;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}
}
