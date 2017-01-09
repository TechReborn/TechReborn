package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;

public class TileDigitalChest extends TileTechStorageBase implements IContainerProvider {

	public TileDigitalChest() {
		super("TileDigitalChest", 32768);
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("digitalchest").player(player.inventory).inventory().hotbar().addInventory()
			.tile(this).slot(0, 80, 24).outputSlot(1, 80, 64).addInventory().create();
	}
}
