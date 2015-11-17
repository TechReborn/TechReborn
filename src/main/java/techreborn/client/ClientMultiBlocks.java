package techreborn.client;


import reborncore.client.multiblock.Multiblock;
import techreborn.init.ModBlocks;

public class ClientMultiBlocks {

   public static Multiblock reactor;

    public static void init(){
        reactor = new Multiblock();
        checkCoils();
    }

    public static void checkCoils() {
        if ((isCoil(3, 0, 1)) &&
                (isCoil(3, 0, 0)) &&
                (isCoil(3, 0, 0 - 1)) &&
                (isCoil(0 - 3, 0, 1)) &&
                (isCoil(0 - 3, 0, 0)) &&
                (isCoil(0 - 3, 0, 0 - 1)) &&
                (isCoil(2, 0, 2)) &&
                (isCoil(2, 0, 1)) &&
                (isCoil(2, 0, 0 - 1)) &&
                (isCoil(2, 0, 0 - 2)) &&
                (isCoil(0 - 2, 0, 2)) &&
                (isCoil(0 - 2, 0, 1)) &&
                (isCoil(0 - 2, 0, 0 - 1)) &&
                (isCoil(0 - 2, 0, 0 - 2)) &&
                (isCoil(1, 0, 3)) &&
                (isCoil(1, 0, 2)) &&
                (isCoil(1, 0, 0 - 2)) &&
                (isCoil(1, 0, 0 - 3)) &&
                (isCoil(0 - 1, 0, 3)) &&
                (isCoil(0 - 1, 0, 2)) &&
                (isCoil(0 - 1, 0, 0 - 2)) &&
                (isCoil(0 - 1, 0, 0 - 3)) &&
                (isCoil(0, 0, 3)) &&
                (isCoil(0, 0, 0 - 3))) {
        }
    }

    private static boolean isCoil(int x, int y, int z) {
        reactor.addComponent(x, y, z, ModBlocks.FusionCoil, 0);
        return true;
    }
    
}
