package techreborn.power;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.api.power.IEnergyInterfaceTile;
import techreborn.parts.CableMultipart;
import techreborn.parts.EnumCableType;

import java.util.ArrayList;
import java.util.List;

public class TRPowerNet {
    int tick = 0;
    private ArrayList<CableMultipart> cables = new ArrayList();
    public ArrayList<EnergyHandler> endpoints = new ArrayList();
    private int energy = 0;
    EnumCableType cableType;

    public TRPowerNet(EnumCableType cableType) {
        this.cableType = cableType;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public int getIOLimit() {
        return cableType.transferRate;
    }

    @SubscribeEvent
    public void tick(PowerTickEvent evt) {
        if (tick < 20) {
            tick++;
            return;
        }
        if (tick % 80 == 0) {
            List<CableMultipart> oldCables = new ArrayList<>();
            for (CableMultipart cableMultipart : cables) {
                if (cableMultipart.getWorld() == null || cableMultipart.getPos() == null) {
                    oldCables.add(cableMultipart);
                }
                CableMultipart mp = cableMultipart.getPartFromWorld(cableMultipart.getWorld(), cableMultipart.getPos(), null);
                if (mp == null) {
                    oldCables.add(cableMultipart);
                }
            }
            cables.removeAll(oldCables);
        }
        if (!cables.isEmpty()) {
            ArrayList<EnergyHandler> collectibles = new ArrayList();
            ArrayList<EnergyHandler> insertibles = new ArrayList();
            for (int i = 0; i < endpoints.size(); i++) {
                EnergyHandler ei = endpoints.get(i);
                if (ei.isCollectible()) {
                    collectibles.add(ei);
                }
                if (ei.isInsertible()) {
                    insertibles.add(ei);
                }
            }

            for (EnergyHandler handler : collectibles) {
                energy += handler.collectEnergy(cableType.transferRate);
            }

            for (EnergyHandler handler : insertibles) {
                energy -= handler.addEnergy(Math.min(energy, cableType.transferRate));
            }
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
        tick++;
    }

    public void addElement(CableMultipart te) {
        if (!cables.contains(te)) {
            cables.add(te);
        }
    }

    public void removeElement(CableMultipart te) {
        cables.remove(te);
        this.rebuild();
    }

    private void rebuild() {
        for (int i = 0; i < cables.size(); i++) {
            CableMultipart te = cables.get(i);
            te.setNetwork(null);
            te.findAndJoinNetwork(te.getWorld(), te.getPos());
        }
        this.clear(true);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void addConnection(IEnergyInterfaceTile ih, EnumFacing dir) {
        if (ih instanceof CableMultipart)
            return;
        EnergyHandler has = this.getHandleFrom(ih);
        if (has == null) {
            endpoints.add(new EnergyHandler(ih, cableType, dir));
        } else {
            has.side = dir;
        }
    }

    public void merge(TRPowerNet n) {
        if (n != this) {
            ArrayList<CableMultipart> li = new ArrayList();
            for (int i = 0; i < n.cables.size(); i++) {
                CableMultipart wire = n.cables.get(i);
                li.add(wire);
            }
            for (int i = 0; i < n.endpoints.size(); i++) {
                EnergyHandler ei = n.endpoints.get(i);
                EnergyHandler has = this.getHandleFrom(ei.tile);
                if (has == null) {
                    endpoints.add(ei);
                }
            }
            n.clear(false);
            for (int i = 0; i < li.size(); i++) {
                CableMultipart wire = li.get(i);
                wire.setNetwork(this);
            }
            MinecraftForge.EVENT_BUS.unregister(n);
        }
    }

    private EnergyHandler getHandleFrom(IEnergyInterfaceTile tile) {
        for (int i = 0; i < endpoints.size(); i++) {
            EnergyHandler ei = endpoints.get(i);
            if (ei.contains(tile))
                return ei;
        }
        return null;
    }

    private void clear(boolean clearTiles) {
        if (clearTiles) {
            for (int i = 0; i < cables.size(); i++) {
                cables.get(i).resetNetwork();
            }
        }

        cables.clear();
        endpoints.clear();
        energy = 0;

        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public String toString() {
        return cables.size() + ": " + endpoints.toString();
    }

    public int addEnergy(int maxAdd, boolean simulate) {
        if (energy >= this.getIOLimit())
            return 0;
        maxAdd = Math.min(this.getIOLimit(), maxAdd);
        if (!simulate)
            energy += maxAdd;
        return maxAdd;
    }

    private static class EnergyHandler {
        private final IEnergyInterfaceTile tile;
        private EnumFacing side;
        private final EnumCableType type;

        private EnergyHandler(IEnergyInterfaceTile ih, EnumCableType type, EnumFacing dir) {
            tile = ih;
            this.type = type;
            this.side = dir;
        }

        public boolean isInsertible() {
            return this.getTotalInsertible() > 0;
        }

        public boolean isCollectible() {
            return this.getTotalCollectible() > 0;
        }

        public boolean contains(IEnergyInterfaceTile tile) {
            return tile == this.tile;
        }

        public int collectEnergy(int max) {
            int total = 0;
            if (tile.canProvideEnergy(EnumFacing.NORTH)) {
                int collect = (int) Math.min(max, tile.getMaxOutput());
                total = (int) tile.useEnergy(collect, false);
            }
            return total;
        }

        public int addEnergy(int max) {
            int total = 0;
            if (tile.canAcceptEnergy(EnumFacing.NORTH)) {
                if (type.tier.ordinal() > tile.getTier().ordinal()) {
                    if (tile instanceof TileEntity) {
                        ((TileEntity) tile).getWorld().createExplosion(new EntityTNTPrimed(((TileEntity) tile).getWorld()), ((TileEntity) tile).getPos().getX(), ((TileEntity) tile).getPos().getY(), ((TileEntity) tile).getPos().getZ(), 2.5F, true);
                    }
                    return 0;
                }
                int add = max - total;
                total += tile.addEnergy(add, false);
            }
            return total;
        }

        public int getTotalCollectible() {
            if (tile.canProvideEnergy(EnumFacing.NORTH)) {
                return (int) Math.min(tile.getMaxOutput(), tile.getEnergy());
            }
            return 0;
        }

        public int getTotalInsertible() {
            int total = 0;
            if (tile.canAcceptEnergy(EnumFacing.NORTH)) {
                total += tile.addEnergy(type.transferRate, true);
            }

            return total;
        }

        @Override
        public String toString() {
            return tile + " @ " + side;
        }
    }
}
