package techreborn.power;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.api.power.IEnergyInterfaceTile;
import techreborn.parts.powerCables.CableMultipart;
import techreborn.parts.powerCables.EnumCableType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TRPowerNet {
	public ArrayList<EnergyHandler> endpoints = new ArrayList();
	int tick = 0;
	EnumCableType cableType;
	private ArrayList<CableMultipart> cables = new ArrayList();
	private int energy = 0;

	public TRPowerNet(EnumCableType cableType) {
		this.cableType = cableType;
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void buildEndpoint(TRPowerNet net) {
		ArrayList<CableMultipart> parts = new ArrayList<>();
		ArrayList<CableMultipart> partsToMerge = new ArrayList<>();
		parts.addAll(net.cables);
		for (CableMultipart cable : parts) {
			for (EnumFacing facing : EnumFacing.VALUES) {
				BlockPos pos = cable.getPos().offset(facing);
				TileEntity tile = cable.getWorld().getTileEntity(pos);
				if (tile != null && tile instanceof IEnergyInterfaceTile) {
					IEnergyInterfaceTile eit = (IEnergyInterfaceTile) tile;
					net.addConnection(eit, facing);
				}
				CableMultipart cableMultipart = CableMultipart.getPartFromWorld(cable.getWorld(), pos, null);
				if (cableMultipart != null) {
					if (cableMultipart.getNetwork() != net) {
						partsToMerge.add(cableMultipart);
					}
				}
			}
		}
		for (CableMultipart cableMultipart : partsToMerge) {
			cableMultipart.mergeWith = net;
		}
		net.checkAndRemoveOldEndpoints();
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
				CableMultipart mp = cableMultipart.getPartFromWorld(cableMultipart.getWorld(), cableMultipart.getPos(),
					null);
				if (mp == null) {
					oldCables.add(cableMultipart);
				}
			}
			cables.removeAll(oldCables);
		}
		if (!cables.isEmpty()) {
			ArrayList<EnergyHandler> collectibles = new ArrayList();
			ArrayList<EnergyHandler> insertibles = new ArrayList();
			for (EnergyHandler ei : endpoints) {
				if (ei.isCollectible()) {
					collectibles.add(ei);
				}
				if (ei.isInsertible()) {
					insertibles.add(ei);
				}
			}

			if (energy < cableType.transferRate * cables.size()) {
				for (EnergyHandler handler : collectibles) {
					energy += handler.collectEnergy(cableType.transferRate);
				}
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
		this.checkAndRemoveOldEndpoints();
	}

	public void checkAndRemoveOldEndpoints() {
		List<EnergyHandler> deadHandlers = new ArrayList<>();
		for (EnergyHandler energyHandler : endpoints) {
			TileEntity tile = (TileEntity) energyHandler.tile;
			if (tile.getWorld().getTileEntity(tile.getPos()) == null) {
				deadHandlers.add(energyHandler);
			} else {
				boolean hasNet = false;
				for (EnumFacing facing : EnumFacing.VALUES) {
					BlockPos pos = tile.getPos().offset(facing);
					CableMultipart multipart = CableMultipart.getPartFromWorld(tile.getWorld(), pos, facing);
					if (multipart != null && multipart.getNetwork() == this) {
						hasNet = true;
					}
				}
				if (!hasNet) {
					deadHandlers.add(energyHandler);
				}
			}

		}
		for (Iterator<EnergyHandler> it = endpoints.iterator(); it.hasNext(); ) {
			EnergyHandler energyHandler = it.next();
			if (deadHandlers.contains(energyHandler)) {
				it.remove();
			}
		}
	}

	public void rebuild() {
		for (int i = 0; i < cables.size(); i++) {
			CableMultipart te = cables.get(i);
			te.setNetwork(null);
			te.findAndJoinNetwork(te.getWorld(), te.getPos());
		}
		this.clear(true);
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		energy += energy;
		if (energy < 0) {
			energy = 0;
		}
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
			for (EnergyHandler ei : n.endpoints) {
				endpoints.add(ei);
			}
			n.clear(false);
			for (int i = 0; i < li.size(); i++) {
				CableMultipart wire = li.get(i);
				wire.setNetwork(this);
			}
			checkAndRemoveOldEndpoints();
			MinecraftForge.EVENT_BUS.unregister(n);
		}
	}

	private EnergyHandler getHandleFrom(IEnergyInterfaceTile tile) {
		for (EnergyHandler ei : endpoints) {
			if (ei.tile == tile)
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
		for (Iterator<TRPowerNet.EnergyHandler> it = endpoints.iterator(); it.hasNext(); ) {
			it.next();
			it.remove();
		}
		energy = 0;

		MinecraftForge.EVENT_BUS.unregister(this);
	}

	public int addEnergy(int maxAdd, boolean simulate) {
		if (energy >= this.getIOLimit())
			return 0;
		maxAdd = Math.min(this.getIOLimit(), maxAdd);
		if (!simulate)
			energy += maxAdd;
		return maxAdd;
	}

	public static class EnergyHandler {
		private final IEnergyInterfaceTile tile;
		private final EnumCableType type;
		private EnumFacing side;

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
			if (tile.canProvideEnergy(side.getOpposite())) {
				int collect = (int) Math.min(max, Math.min(tile.getMaxOutput(), tile.getEnergy()));
				total = (int) tile.useEnergy(collect, false);
			}
			return total;
		}

		public int addEnergy(int max) {
			int total = 0;
			if (tile.canAcceptEnergy(side.getOpposite()) && max > 0) {
				if (type.tier.ordinal() > tile.getTier().ordinal() && max > tile.getMaxInput()) {
					if (tile instanceof TileEntity) {
						((TileEntity) tile).getWorld().createExplosion(
							new EntityTNTPrimed(((TileEntity) tile).getWorld()),
							((TileEntity) tile).getPos().getX(), ((TileEntity) tile).getPos().getY(),
							((TileEntity) tile).getPos().getZ(), 2.5F, true);
					}
					return 0;
				}
				int add = max - total;
				total += tile.addEnergy(add, false);
			}
			return total;
		}

		public int getTotalCollectible() {
			if (tile.canProvideEnergy(side.getOpposite()) && tile.getEnergy() != 0) {
				return (int) Math.min(tile.getMaxOutput(), tile.getEnergy());
			}
			return 0;
		}

		public int getTotalInsertible() {
			int total = 0;
			if (tile.canAcceptEnergy(side.getOpposite()) && tile.getMaxPower() - tile.getEnergy() != 0) {
				total += tile.addEnergy(type.transferRate, true);
			}

			return total;
		}
	}
}
