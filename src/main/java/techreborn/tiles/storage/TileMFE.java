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
public class TileMFE extends TileEnergyStorage implements IContainerProvider {

	public TileMFE() {
		super("MFE", 2, ModBlocks.MVSU, EnumPowerTier.MEDIUM, 512, 512, 600000);
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("mfe").player(player.inventory).inventory().hotbar().armor()
			.complete(8, 18).addArmor().addInventory().tile(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
			.syncEnergyValue().addInventory().create();
	}

}