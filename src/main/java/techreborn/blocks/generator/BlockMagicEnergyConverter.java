package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.client.TechRebornCreativeTab;

public class BlockMagicEnergyConverter extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockMagicEnergyConverter(Material material) {
		super();
		setUnlocalizedName("techreborn.magicenergyconverter");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public String getFront(boolean isActive) {
		return prefix + "magic_energy_converter_front_off";
	}

	@Override
	public String getSide(boolean isActive) {
		return prefix + "magic_energy_converter_side";
	}

	@Override
	public String getTop(boolean isActive) {
		return prefix + "magic_energy_converter_top";
	}

	@Override
	public String getBottom(boolean isActive) {
		return prefix + "magic_energy_converter_bottom";
	}

}
