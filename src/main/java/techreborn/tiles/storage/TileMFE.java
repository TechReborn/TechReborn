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
		return new ContainerBuilder("mfe").player(player.inventory).inventory(8, 84).hotbar(8, 142).armor()
				.complete(44, 6).addArmor().addInventory().tile(this).energySlot(0, 80, 17).energySlot(1, 80, 53)
				.syncEnergyValue().addInventory().create();
	}

}