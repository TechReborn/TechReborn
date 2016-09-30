package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileDragonEggSiphoner;

public class BlockDragonEggSiphoner extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockDragonEggSiphoner(Material material) {
		super();
		setUnlocalizedName("techreborn.dragoneggsiphoner");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileDragonEggSiphoner();
	}

	@Override
	public String getFront(boolean isActive) {
		return prefix + "dragon_egg_energy_siphon_side_off";
	}

	@Override
	public String getSide(boolean isActive) {
		return prefix + "dragon_egg_energy_siphon_side_off";
	}

	@Override
	public String getTop(boolean isActive) {
		return prefix + "dragon_egg_energy_siphon_top";
	}

	@Override
	public String getBottom(boolean isActive) {
		return prefix + "generator_machine_bottom";
	}

}
