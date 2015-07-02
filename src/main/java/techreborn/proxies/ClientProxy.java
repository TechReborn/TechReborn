package techreborn.proxies;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import techreborn.client.IconSupplier;
import techreborn.client.hud.ChargeHud;
import techreborn.client.keybindings.KeyBindings;
import techreborn.tiles.idsu.ClientSideIDSUManager;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() 
    {
        super.init();
        MinecraftForge.EVENT_BUS.register(new IconSupplier());
        MinecraftForge.EVENT_BUS.register(new ChargeHud());
        ClientRegistry.registerKeyBinding(KeyBindings.config);
		MinecraftForge.EVENT_BUS.register(new ClientSideIDSUManager());
    }
}
