package techreborn.proxies;

import net.minecraftforge.common.MinecraftForge;
import techreborn.client.IconSupplier;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new IconSupplier());
    }
}
