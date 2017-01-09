package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;

public class TileQuantumChest extends TileTechStorageBase implements IContainerProvider {

	public TileQuantumChest() {
		super("TileQuantumChest", Integer.MAX_VALUE);
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("quantumchest").player(player.inventory).inventory().hotbar().addInventory()
			.tile(this).slot(0, 80, 24).outputSlot(1, 80, 64).addInventory().create();
	}
}
