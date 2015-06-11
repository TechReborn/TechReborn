package techreborn.proxies;

import net.minecraftforge.common.MinecraftForge;
import techreborn.client.IconSupplier;
import techreborn.client.hud.ChargeHud;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() 
    {
        super.init();
        MinecraftForge.EVENT_BUS.register(new IconSupplier());
        MinecraftForge.EVENT_BUS.register(new ChargeHud());
    }
}
