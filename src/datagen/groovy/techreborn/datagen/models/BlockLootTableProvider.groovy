package techreborn.datagen.models

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider
import net.minecraft.data.DataWriter
import net.minecraft.data.server.BlockLootTableGenerator
import net.minecraft.loot.LootTable
import net.minecraft.util.Identifier
import org.jetbrains.annotations.NotNull
import techreborn.init.TRContent

import java.util.function.BiConsumer
import java.util.function.Consumer

class BlockLootTableProvider extends FabricBlockLootTableProvider{

	BlockLootTableProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	protected void generateBlockLootTables() {
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
