package techreborn.tiles.storage;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.api.power.EnumPowerTier;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class TileBatBox extends TileEnergyStorage implements IContainerProvider {

	public TileBatBox() {
		super("BatBox", 2, ModBlocks.BATTERY_BOX, EnumPowerTier.LOW, 32, 32, 40000);
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("batbox").player(player.inventory).inventory().hotbar().addInventory()
			.tile(this).energySlot(0, 62, 45).energySlot(1, 98, 45).syncEnergyValue().addInventory().create();
	}
}
