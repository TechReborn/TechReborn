package techreborn.datagen.models

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import techreborn.init.TRContent

class BlockLootTableProvider extends FabricBlockLootTableProvider{

	BlockLootTableProvider(FabricDataOutput dataOutput) {
		super(dataOutput)
	}

	@Override
	public void generate() {
		TRContent.StorageBlocks.values().each {
			addDrop(it.getBlock())
			addDrop(it.getSlabBlock())
			addDrop(it.getStairsBlock())
			addDrop(it.getWallBlock())
		}
		TRContent.Cables.values().each {
			addDrop(it.block)
		}
		TRContent.Machine.values().each {
			addDrop(it.block)
		}
		TRContent.SolarPanels.values().each {
			addDrop(it.block)
		}
		TRContent.StorageUnit.values().each {
			addDrop(it.block)
		}
		TRContent.TankUnit.values().each {
			addDrop(it.block)
		}
		TRContent.MachineBlocks.values().each {
			addDrop(it.getFrame())
			addDrop(it.getCasing())
		}
	}
}
