package techreborn.proxies;

import net.minecraftforge.common.MinecraftForge;
import techreborn.client.IconSupplier;
import techreborn.partSystem.ModPartRegistry;
import techreborn.partSystem.block.WorldProvider;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() 
    {
        super.init();
        MinecraftForge.EVENT_BUS.register(new IconSupplier());
		ModPartRegistry.addProvider(new WorldProvider());
    }
}
