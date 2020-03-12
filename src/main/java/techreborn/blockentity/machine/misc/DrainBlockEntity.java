package techreborn.blockentity.machine.misc;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import reborncore.api.IListInfoProvider;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.init.TRBlockEntities;

public class DrainBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, IListInfoProvider, BuiltScreenHandlerProvider {

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
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return null;
	}
}
