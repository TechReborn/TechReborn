package techreborn.proxies;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import reborncore.client.multiblock.MultiblockRenderEvent;
import techreborn.client.*;
import techreborn.client.hud.ChargeHud;
import techreborn.client.keybindings.KeyBindings;

public class ClientProxy extends CommonProxy {

    public static MultiblockRenderEvent multiblockRenderEvent;

    @Override
    public void init() {
        super.init();
        RegisterItemJsons.registerModels();
        MinecraftForge.EVENT_BUS.register(new IconSupplier());
        MinecraftForge.EVENT_BUS.register(new ChargeHud());
        MinecraftForge.EVENT_BUS.register(new VersionCheckerClient());
        MinecraftForge.EVENT_BUS.register(new StackToolTipEvent());
        multiblockRenderEvent = new MultiblockRenderEvent();
        MinecraftForge.EVENT_BUS.register(multiblockRenderEvent);
        ClientRegistry.registerKeyBinding(KeyBindings.config);
        ClientMultiBlocks.init();
    }
}
