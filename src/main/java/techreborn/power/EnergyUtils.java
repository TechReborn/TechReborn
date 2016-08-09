package techreborn.power;

import cofh.api.energy.IEnergyReceiver;
import com.google.common.collect.Lists;
import ic2.api.energy.tile.IEnergySink;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.api.power.tile.IEnergyReceiverTile;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.mcmultipart.block.TileMultipartContainer;
import reborncore.mcmultipart.multipart.ISlottedPart;
import reborncore.mcmultipart.multipart.PartSlot;
import techreborn.parts.powerCables.CableMultipart;
import techreborn.parts.powerCables.EnumCableType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;

public class EnergyUtils {


    /**
     * Same as {@link #dispatchWiresEnergyPacketRecursively(World, ArrayList, BlockPos, boolean, double)}
     * But not simulated and excluding source block
     * @param world the world to dispatch in
     * @param energyAmount energy units to dispatch
     * @return dispatched energy units
     * @deprecated for internal use only
     *
     * WARNING: For internal use in wires only
     */
    public static double dispatchWiresEnergyPacket(World world, BlockPos source, double energyAmount, BlockPos... excludes) {
        ArrayList<BlockPos> exclude = new ArrayList<>(excludes.length + 1);
        exclude.addAll(Arrays.asList(excludes));
        exclude.add(source);
        return dispatchWiresEnergyPacketRecursively(world, exclude, source, false, energyAmount);
    }

    /**
     * Dispatches energy packet recursively through wires to all connected decives
     * @param world the world to dispatch in
     * @param excludes blocks to exclude from checking. as example, start block
     * @param blockPos block pos to start checking
     * @param maxEnergy energy units to dispatch
     * @param simulate true if dispatch is only simulated
     * @return dispatched energy units
     * @deprecated for internal use only
     *
     * WARNING: For internal use in wires only
     */
    public static double dispatchWiresEnergyPacketRecursively(World world, ArrayList<BlockPos> excludes, BlockPos blockPos, boolean simulate, final double maxEnergy) {
        double energyLeft = maxEnergy;
        Collection<EnumFacing> connections = Lists.newArrayList(EnumFacing.VALUES);

        //Workaround for multipart cables
        CableMultipart selfCable = getWire(world, blockPos);

        if(selfCable != null) {
            connections = selfCable.connectedSides.keySet();
        }

        TileEntity tileEntity = world.getTileEntity(blockPos);
        if(tileEntity instanceof TilePowerAcceptor) {
            for(EnumFacing facing : EnumFacing.VALUES) {
                if(!((TilePowerAcceptor) tileEntity).canProvideEnergy(facing))
                    connections.remove(facing);
            }
        }

        for(EnumFacing facing : connections) {
            BlockPos offsetPos = blockPos.offset(facing);
            if(!excludes.contains(offsetPos)) {
                excludes.add(offsetPos);
                if(isWire(world, offsetPos)) {
                    CableMultipart cable = getWire(world, offsetPos);
                    EnumCableType cableType = cable.getCableType();
                    double transferRate = cableType.transferRate;
                    double maxPacket = transferRate > maxEnergy ? maxEnergy : transferRate;
                    energyLeft -= dispatchWiresEnergyPacketRecursively(world, excludes, offsetPos, simulate, maxPacket);
                    if(energyLeft == 0) {
                        if(selfCable != null)
                            selfCable.lastEnergyPacket = maxEnergy * 1.3F;
                        return maxEnergy;
                    }
                } else {
                    PowerNetReceiver receiver = getReceiver(world, facing.getOpposite(), offsetPos);
                    if(receiver != null) {
                        energyLeft -= receiver.receiveEnergy(energyLeft, simulate);
                        if(energyLeft == 0) {
                            if(selfCable != null)
                                selfCable.lastEnergyPacket = maxEnergy * 1.3F;
                            return maxEnergy;
                        }
                    }
                }
            }
        }
        double energyDispatched = maxEnergy - energyLeft;
        if(selfCable != null)
            selfCable.lastEnergyPacket = energyDispatched * 1.3F;
        return energyDispatched;
    }


    //////////////////// Wire utilities ////////////////////

    public static boolean isWire(World world, BlockPos blockPos) {
        return getWire(world, blockPos) != null;
    }

    public static CableMultipart getWire(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileMultipartContainer) {
            TileMultipartContainer multipart = (TileMultipartContainer) tileEntity;
            ISlottedPart centerPart = multipart.getPartInSlot(PartSlot.CENTER);
            if(centerPart instanceof CableMultipart)
                return (CableMultipart) centerPart;
        }
        return null;
    }

    //////////////////// Power Net Sources functionality ////////////////////
    //TODO




    //////////////////// Power Net Receivers functionality ////////////////////

    public static PowerNetReceiver getReceiver(World world, EnumFacing side, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (Loader.isModLoaded("IC2") && RebornCoreConfig.getRebornPower().eu()) {
            PowerNetReceiver ic2receiver = getIC2Receiver(tileEntity, side);
            if (ic2receiver != null) return ic2receiver;
        }
        if (Loader.isModLoaded("Tesla") && RebornCoreConfig.getRebornPower().tesla()) {
            PowerNetReceiver teslaReceiver = getTeslaReceiver(tileEntity, side);
            if (teslaReceiver != null) return teslaReceiver;
        }
        if (RebornCoreConfig.getRebornPower().rf()) {
            PowerNetReceiver rfReceiver = getRFReceiver(tileEntity, side);
            if (rfReceiver != null) return rfReceiver;
        }
        PowerNetReceiver internalReceiver = getInternalReceiver(tileEntity, side);
        if (internalReceiver != null) return internalReceiver;
        return null;
    }

    public static PowerNetReceiver getInternalReceiver(TileEntity tileEntity, EnumFacing side) {
        if(tileEntity instanceof IEnergyReceiverTile) {
            IEnergyReceiverTile energyInterface = (IEnergyReceiverTile) tileEntity;
            return new PowerNetReceiver(
                    (energy, simulated) -> {
                        if(energyInterface.canAcceptEnergy(side))
                            return energyInterface.addEnergy(energy, simulated);
                        return 0.0;
                    });
        }
        return null;
    }

    public static PowerNetReceiver getIC2Receiver(TileEntity tileEntity, EnumFacing side) {
        if(tileEntity instanceof IEnergySink) {
            IEnergySink acceptor = (IEnergySink) tileEntity;
            return new PowerNetReceiver(
                    (energy, simulated) -> {
                        if(simulated) {
                            double max = acceptor.getDemandedEnergy();
                            return max > energy ? energy : max;
                        }
                        return energy - acceptor.injectEnergy(side, energy, acceptor.getSinkTier());
                    });
        }
        return null;
    }

    public static PowerNetReceiver getTeslaReceiver(TileEntity tileEntity, EnumFacing side) {
        if(tileEntity.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, side)) {
            ITeslaConsumer consumer = tileEntity.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, side);
            return new PowerNetReceiver(
                    (energy, simulated) -> {
                        long teslaEnergy = eu2tesla(energy);
                        return tesla2eu(consumer.givePower(teslaEnergy, simulated));
            });
        }
        return null;
    }

    public static PowerNetReceiver getRFReceiver(TileEntity tileEntity, EnumFacing side) {
        if(tileEntity instanceof IEnergyReceiver) {
            IEnergyReceiver receiver = (IEnergyReceiver) tileEntity;
            return new PowerNetReceiver(
                    (energy, simulated) -> {
                        int rfEnergy = eu2rf(energy);
                        return rf2eu(receiver.receiveEnergy(side, rfEnergy, simulated));
                    });
        }
        return null;
    }


    //////////////////// Energy Conversion utils ////////////////////

    public static double rf2eu(int rf) {
        return rf / 4F;
    }

    public static int eu2rf(double eu) {
        return (int) Math.floor(eu * 4F);
    }

    public static long eu2tesla(double eu) {
        return (long) Math.floor(eu / 3.75F);
    }

    public static double tesla2eu(long tesla) {
        return tesla * 3.75F;
    }


    public static class PowerNetReceiver {

        private final BiFunction<Double, Boolean, Double> receiveEnergy;

        public PowerNetReceiver(BiFunction<Double, Boolean, Double> receiveEnergy) {
            this.receiveEnergy = receiveEnergy;
        }

        /**
         * Sends given amount fo EU to receiver
         * @param max max eu to receive
         * @return eu received
         */
        public double receiveEnergy(double max, boolean simulated) {
            return receiveEnergy.apply(max, simulated);
        }

    }

}
