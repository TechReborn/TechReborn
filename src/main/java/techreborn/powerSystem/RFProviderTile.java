package techreborn.powerSystem;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.api.power.IEnergyInterfaceTile;
import techreborn.config.ConfigTechReborn;
import techreborn.tiles.TileMachineBase;

import java.util.Random;

/**
 * This is done in a different class so the updateEntity can be striped for ic2 and this one will still get called.
 */
public abstract class RFProviderTile extends TileMachineBase implements IEnergyReceiver, IEnergyProvider, IEnergyInterfaceTile {


    Random random = new Random();

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) {
            return;
        }
        sendPower();
    }

    public void sendPower() {
        if (!PowerSystem.RFPOWENET) {
            return;
        }
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            int extracted = getEnergyStored(direction);

            TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (isPoweredTile(tile, direction)) {
                if (canProvideEnergy(direction)) {
                    if (tile instanceof IEnergyHandler) {
                        IEnergyHandler handler = (IEnergyHandler) tile;
                        int neededRF = handler.receiveEnergy(
                                direction.getOpposite(),
                                extracted, false);

                        extractEnergy(direction.getOpposite(), neededRF, false);
                    } else if (tile instanceof IEnergyReceiver) {
                        IEnergyReceiver handler = (IEnergyReceiver) tile;
                        int neededRF = handler.receiveEnergy(
                                direction.getOpposite(),
                                extracted, false);

                        extractEnergy(direction.getOpposite(), neededRF, false);
                    }
                }

            }
        }
    }

    public boolean isPoweredTile(TileEntity tile, ForgeDirection side) {
        if (tile == null) {
            return false;
        } else if (tile instanceof IEnergyHandler || tile instanceof IEnergyReceiver) {
            return ((IEnergyConnection) tile).canConnectEnergy(side.getOpposite());
        } else {
            return false;
        }
    }
}
