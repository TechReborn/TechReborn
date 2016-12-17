package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.api.power.EnumPowerTier;
import techreborn.api.generator.EFluidGenerator;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileSemifluidGenerator extends TileBaseFluidGenerator {

	public TileSemifluidGenerator() {
		super(EFluidGenerator.SEMIFLUID, ConfigTechReborn.ThermalGeneratorTier, "TileSemifluidGenerator", 1000*10, 8);
	}
	
	@Override
	public ItemStack getWrenchDrop(EntityPlayer arg0) {
		return new ItemStack(ModBlocks.semiFluidGenerator, 1);
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}
	
	@Override
	public double getMaxPower() {
		return ConfigTechReborn.ThermalGeneratorCharge;
	}
}
