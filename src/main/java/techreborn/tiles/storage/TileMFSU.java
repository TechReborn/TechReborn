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
public class TileMFSU extends TileEnergyStorage implements IContainerProvider {

	public TileMFSU() {
		super("MFSU", 2, ModBlocks.HVSU, EnumPowerTier.HIGH, 2048, 2048, 1000000);
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("mfsu").player(player.inventory).inventory().hotbar().armor()
			.complete(8, 18).addArmor().addInventory().tile(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
			.syncEnergyValue().addInventory().create();
	}
}