package techreborn.init;

import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;

// Class containing definitions of burnable materials
public class FuelRecipes {
	public static void init() {
		FuelRegistryImpl registry = FuelRegistryImpl.INSTANCE;

		// Basing it off https://minecraft.gamepedia.com/Furnace/table

		// Rubber spam
		registry.add(TRContent.RUBBER_BUTTON, 300);
		registry.add(TRContent.RUBBER_LOG, 300);
		registry.add(TRContent.RUBBER_LOG_STRIPPED, 300);
		registry.add(TRContent.RUBBER_WOOD, 300);
		registry.add(TRContent.STRIPPED_RUBBER_WOOD, 300);
		registry.add(TRContent.RUBBER_PLANKS, 300);
		registry.add(TRContent.RUBBER_PLANK_SLAB, 150);
		registry.add(TRContent.RUBBER_FENCE, 300);
		registry.add(TRContent.RUBBER_FENCE_GATE, 300);
		registry.add(TRContent.RUBBER_PLANK_STAIR, 300);
		registry.add(TRContent.RUBBER_TRAPDOOR, 300);
		registry.add(TRContent.RUBBER_PRESSURE_PLATE, 300);
		registry.add(TRContent.RUBBER_DOOR, 200);
		registry.add(TRContent.RUBBER_SAPLING, 100);


		// Other stuff
		registry.add(TRContent.Machine.RESIN_BASIN, 300);
		registry.add(TRContent.Plates.WOOD, 300);
	}
}
