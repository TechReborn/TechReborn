package techreborn.blockentity.machine.misc;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import reborncore.api.IListInfoProvider;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.init.TRBlockEntities;

public class DrainBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, IListInfoProvider, IContainerProvider {

	public DrainBlockEntity(){
		this(TRBlockEntities.DRAIN);
	}

	public DrainBlockEntity(BlockEntityType<?> blockEntityTypeIn) {
		super(blockEntityTypeIn);
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

	@Override
	public BuiltContainer createContainer(int syncID, PlayerEntity player) {
		return null;
	}
}
