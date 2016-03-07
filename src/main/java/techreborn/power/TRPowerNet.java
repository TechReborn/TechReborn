package techreborn.power;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.api.power.IEnergyInterfaceTile;
import techreborn.parts.CableMultipart;

import java.util.ArrayList;

public class TRPowerNet {
	int i = 0;
	private ArrayList<CableMultipart> cables = new ArrayList();
	private ArrayList<EnergyHandler> endpoints = new ArrayList();
	private int energy = 0;
	private int networkLimit = 1000;

	public TRPowerNet() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public int getIOLimit() {
		return networkLimit;
	}

	public void setIOLimit(int limit) {
		if (networkLimit != limit) {
			networkLimit = limit;
			for (int i = 0; i < cables.size(); i++) {
				CableMultipart cable = cables.get(i);
				if (cable.getCableType().transferRate!= networkLimit) {
					networkLimit = cable.getCableType().transferRate;
				}
			}
		}
	}

	@SubscribeEvent
	public void tick(PowerTickEvent evt) {
//		if (i > 10) {
//			for (CableMultipart modCablePart : ModCablePart.partsInWorld) {
//				if (!ModPartUtils.hasCable(modCablePart.world, modCablePart.location)) {
//					ModCablePart.partsInWorld.remove(modCablePart);
//				}
//			}
//		}

		if (!cables.isEmpty()) {
			ArrayList<EnergyHandler> collectibles = new ArrayList();
			ArrayList<EnergyHandler> insertibles = new ArrayList();
			int maxCanPush = energy;
			for (int i = 0; i < endpoints.size(); i++) {
				EnergyHandler ei = endpoints.get(i);
				maxCanPush += ei.getTotalInsertible();
				if (ei.isCollectible()) {
					collectibles.add(ei);
				}
				if (ei.isInsertible()) {
					insertibles.add(ei);
				}
			}

			maxCanPush = Math.min(this.getIOLimit(), maxCanPush);

			for(EnergyHandler handler : collectibles){
				int space = maxCanPush - energy;
				energy += handler.collectEnergy(space);
			}

			for(EnergyHandler handler : insertibles){
				int add = Math.min(energy, 1 + energy / insertibles.size());
				energy -= handler.addEnergy(add);
			}
			System.out.println(energy);
		}
	}

	public void addElement(CableMultipart te) {
		if (!cables.contains(te)) {
			cables.add(te);
			if (te.getCableType().transferRate > 0 && te.getCableType().transferRate != networkLimit) {
				this.setIOLimit(Math.min(te.getCableType().transferRate, this.getIOLimit()));
			}
		}
	}

	public void removeElement(CableMultipart te) {
		cables.remove(te);
		this.rebuild();
	}

	private void rebuild() {
		System.out.println("Remapping RF network " + this);
		for (int i = 0; i < cables.size(); i++) {
			CableMultipart te = cables.get(i);
			te.findAndJoinNetwork(te.getWorld(), te.getPos());
		}
		this.clear(true);
	}

	public void addConnection(IEnergyInterfaceTile ih, EnumFacing dir) {
		if (ih instanceof CableMultipart)
			return;
		EnergyHandler has = this.getHandleFrom(ih);
		if (has == null) {
			endpoints.add(new EnergyHandler(ih, dir));
		} else {
			has.addSide(dir);
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
				} else {
					has.merge(ei);
				}
			}
			n.clear(false);
			for (int i = 0; i < li.size(); i++) {
				CableMultipart wire = li.get(i);
				wire.setNetwork(this);
			}
			if (n.getIOLimit() != 0 && n.networkLimit != networkLimit)
				this.setIOLimit(Math.min(n.getIOLimit(), this.getIOLimit()));
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

		try {
			MinecraftForge.EVENT_BUS.unregister(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return cables.size() + ": " + endpoints.toString();
	}

	public int drainEnergy(int maxReceive, boolean simulate) {
		maxReceive = Math.min(maxReceive, this.getIOLimit());
		int drain = Math.min(maxReceive, energy);
		if (!simulate)
			energy -= drain;
		return drain;
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
		private final ArrayList<EnumFacing> sides = new ArrayList();

		private EnergyHandler(IEnergyInterfaceTile ih, EnumFacing... dirs) {
			tile = ih;
			for (int i = 0; i < dirs.length; i++) {
				this.addSide(dirs[i]);
			}
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

		public void addSide(EnumFacing dir) {
			if (!sides.contains(dir))
				sides.add(dir);
		}

		public void merge(EnergyHandler ei) {
			for (int i = 0; i < ei.sides.size(); i++) {
				this.addSide(ei.sides.get(i));
			}
		}

		public int collectEnergy(int max) {
			int total = 0;
			for (int i = 0; i < sides.size(); i++) {
				EnumFacing dir = sides.get(i);
				if (tile.canProvideEnergy(dir)) {
					int collect = max - total;
					total += tile.useEnergy(collect, false);
				}
			}
			return total;
		}

		public int addEnergy(int max) {
			int total = 0;
			for (int i = 0; i < sides.size(); i++) {
				EnumFacing dir = sides.get(i);
				if (tile.canAcceptEnergy(dir)) {
					int add = max - total;
					total += tile.addEnergy(add, false);
				}
			}
			return total;
		}

		public int getTotalCollectible() {
			int total = 0;
			for (int i = 0; i < sides.size(); i++) {
				EnumFacing dir = sides.get(i);
				if (tile.canProvideEnergy(dir)) {
					total += tile.useEnergy(Integer.MAX_VALUE, true);
				}
			}
			return total;
		}

		public int getTotalInsertible() {
			int total = 0;
			for (int i = 0; i < sides.size(); i++) {
				EnumFacing dir = sides.get(i);
				if (tile.canAcceptEnergy(dir)) {
					total += tile.addEnergy(Integer.MAX_VALUE, true);
				}
			}
			return total;
		}

		@Override
		public String toString() {
			return tile + " @ " + sides;
		}
	}
}
