package techreborn.proxies;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import techreborn.client.IconSupplier;
import techreborn.client.VersionCheckerClient;
import techreborn.client.hud.ChargeHud;
import techreborn.client.keybindings.KeyBindings;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new IconSupplier());
        MinecraftForge.EVENT_BUS.register(new ChargeHud());
        MinecraftForge.EVENT_BUS.register(new VersionCheckerClient());
        ClientRegistry.registerKeyBinding(KeyBindings.config);
    }
}
