package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class TileAutoCraftingTable extends TilePowerAcceptor implements IContainerProvider {
	public TileAutoCraftingTable() {
		super();
	}

	@Override
	public double getBaseMaxPower() {
		return 0;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing enumFacing) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing enumFacing) {
		return false;
	}

	@Override
	public BuiltContainer createContainer(EntityPlayer player) {
		return new ContainerBuilder("autocraftingTable").player(player.inventory).inventory().hotbar().addInventory().create();
	}
}
