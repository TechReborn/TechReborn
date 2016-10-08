package techreborn.client;

import net.minecraft.util.EnumFacing;
import reborncore.client.multiblock.Multiblock;
import techreborn.init.ModBlocks;

public class ClientMultiBlocks {

	public static Multiblock reactor;
	public static Multiblock frezzer;

	public static void init() {
		reactor = new Multiblock();
		checkCoils();

		frezzer = new Multiblock();
		checkMachine();
	}

	public static void checkCoils() {
		if ((isCoil(3, 0, 1)) && (isCoil(3, 0, 0)) && (isCoil(3, 0, 0 - 1)) && (isCoil(0 - 3, 0, 1))
			&& (isCoil(0 - 3, 0, 0)) && (isCoil(0 - 3, 0, 0 - 1)) && (isCoil(2, 0, 2)) && (isCoil(2, 0, 1))
			&& (isCoil(2, 0, 0 - 1)) && (isCoil(2, 0, 0 - 2)) && (isCoil(0 - 2, 0, 2)) && (isCoil(0 - 2, 0, 1))
			&& (isCoil(0 - 2, 0, 0 - 1)) && (isCoil(0 - 2, 0, 0 - 2)) && (isCoil(1, 0, 3)) && (isCoil(1, 0, 2))
			&& (isCoil(1, 0, 0 - 2)) && (isCoil(1, 0, 0 - 3)) && (isCoil(0 - 1, 0, 3)) && (isCoil(0 - 1, 0, 2))
			&& (isCoil(0 - 1, 0, 0 - 2)) && (isCoil(0 - 1, 0, 0 - 3)) && (isCoil(0, 0, 3)) && (isCoil(0, 0, 0 - 3))) {
		}
	}

	private static boolean isCoil(int x, int y, int z) {
		reactor.addComponent(x, y, z, ModBlocks.FusionCoil, 0);
		return true;
	}

	public static void checkMachine() {
		int xDir = EnumFacing.UP.getFrontOffsetX() * 2;
		int yDir = EnumFacing.UP.getFrontOffsetY() * 2;
		int zDir = EnumFacing.UP.getFrontOffsetZ() * 2;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int k = -1; k < 2; k++) {
					if ((i != 0) || (j != 0) || (k != 0)) {
						frezzer.addComponent(xDir + i, yDir + j, zDir + k, ModBlocks.MachineCasing,
							(((i == 0) && (j == 0) && (k != 0)) || ((i == 0) && (j != 0) && (k == 0))
								 || ((i != 0) && (j == 0) && (k == 0)) ? 2 : 1));
					}
				}
			}
		}
	}

}
