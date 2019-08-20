package techreborn.blockentity.data;

import reborncore.client.containerBuilder.builder.ContainerBlockEntityInventoryBuilder;

import java.util.Arrays;
import java.util.function.BiConsumer;

public enum SlotType {
	//Im really not a fan of the way I add the slots to the builder here
	INPUT((builder, slot) -> {
		builder.slot(slot.getId(), slot.getX(), slot.getY());
	}),
	OUTPUT((builder, slot) -> {
		builder.outputSlot(slot.getId(), slot.getX(), slot.getY());
	}),
	ENERGY((builder, slot) -> {
		builder.energySlot(slot.getId(), slot.getX(), slot.getY());
	});

	public static SlotType fromString(String string){
		return Arrays.stream(values())
			.filter(slotType -> slotType.name().equalsIgnoreCase(string))
			.findFirst()
			.orElse(null);
	}

	private BiConsumer<ContainerBlockEntityInventoryBuilder, DataDrivenSlot> slotBiConsumer;

	SlotType(BiConsumer<ContainerBlockEntityInventoryBuilder, DataDrivenSlot> slotBiConsumer) {
		this.slotBiConsumer = slotBiConsumer;
	}

	public BiConsumer<ContainerBlockEntityInventoryBuilder, DataDrivenSlot> getSlotBiConsumer() {
		return slotBiConsumer;
	}
}
