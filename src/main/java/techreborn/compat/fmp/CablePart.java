package techreborn.compat.fmp;

import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.IC2;
import ic2.core.ITickCallback;
import ic2.core.block.wiring.TileEntityCable;
import ic2.core.block.wiring.TileEntityLuminator;
import ic2.core.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class CablePart extends TMultiPart implements IEnergyConductor, JNormalOcclusion {

	public boolean addedToEnergyNet = false;

	@Override
	public String getType() {
		return "TRCable";
	}

	@Override
	public double getConductionLoss() {
		return 1D;
	}

	@Override
	public double getInsulationEnergyAbsorption() {
		return 2048;
	}

	@Override
	public double getInsulationBreakdownEnergy() {
		return 9001.0D;
	}

	@Override
	public double getConductorBreakdownEnergy() {
		return 2048;
	}

	@Override
	public void removeInsulation() {

	}

	@Override
	public void removeConductor() {

	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity te, ForgeDirection forgeDirection) {
		return canInteractWith(te);
	}

	public boolean canInteractWith(TileEntity te) {
		return !(te instanceof IEnergyTile)?false:(te instanceof TileEntityLuminator?((TileEntityLuminator)te).canCableConnectFrom(this.x(), this.y(), this.z()):true);
	}

	@Override
	public boolean emitsEnergyTo(TileEntity tileEntity, ForgeDirection forgeDirection) {
		return canInteractWith(tileEntity);
	}


	@Override
	public void update() {
		super.update();
	}

	@Override
	public void onChunkUnload() {
		if(IC2.platform.isSimulating() && this.addedToEnergyNet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			this.addedToEnergyNet = false;
		}
		super.onChunkUnload();
	}

	@Override
	public void onChunkLoad() {
		super.onChunkLoad();
		if(IC2.platform.isSimulating()) {

			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.addedToEnergyNet = true;
			this.onNeighborChanged();

		}
	}

	@Override
	public boolean occlusionTest(TMultiPart npart) {
		return NormalOcclusionTest.apply(this, npart);
	}

	@Override
	public Iterable<Cuboid6> getOcclusionBoxes() {
		List<Cuboid6> cubes = new ArrayList<Cuboid6>();
		cubes.add(new Cuboid6(0, 0, 0, 1, 1, 1));
		return cubes;
	}


}
