package techreborn.events;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import techreborn.init.ModBlocks;

public class RegistryFix {

	@SubscribeEvent
	public void fixBlocks(RegistryEvent.MissingMappings<Block> registryEvent) {
		for (RegistryEvent.MissingMappings.Mapping<Block> mapping : registryEvent.getMappings()) {
			if (mapping.key.toString().equals("techreborn:assembly_machine")) {
				mapping.remap(ModBlocks.ASSEMBLING_MACHINE);
			}
		}
	}

	@SubscribeEvent
	public void fixItems(RegistryEvent.MissingMappings<Item> registryEvent) {
		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : registryEvent.getMappings()) {
			if (mapping.key.toString().equals("techreborn:assembly_machine")) {
				mapping.remap(Item.getItemFromBlock(ModBlocks.ASSEMBLING_MACHINE));
			}
		}
	}
}
