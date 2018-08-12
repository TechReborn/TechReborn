package techreborn.tiles;

public class TileCreativeQuantumTank extends TileQuantumTank {

	@Override
	public void update() {
		super.update();
		if (!tank.isEmpty() && !tank.isFull()) {
			tank.setFluidAmount(Integer.MAX_VALUE);
		}
	}

	@Override
	public int slotTransferSpeed() {
		return 1;
	}

	@Override
	public int fluidTransferAmount() {
		return 10000;
	}
}
