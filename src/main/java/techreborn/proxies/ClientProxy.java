package techreborn.proxies;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import reborncore.client.multiblock.MultiblockRenderEvent;
import techreborn.client.ClientMultiBlocks;
import techreborn.client.IconSupplier;
import techreborn.client.RegisterItemJsons;
import techreborn.client.StackToolTipEvent;
import techreborn.client.VersionCheckerClient;
import techreborn.client.hud.ChargeHud;
import techreborn.client.keybindings.KeyBindings;
import techreborn.client.render.entitys.RenderNukePrimed;
import techreborn.entitys.EntityNukePrimed;

public class ClientProxy extends CommonProxy
{

	public static MultiblockRenderEvent multiblockRenderEvent;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		RenderingRegistry.registerEntityRenderingHandler(EntityNukePrimed.class, new RenderManagerNuke());
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		RegisterItemJsons.registerModels();
		MinecraftForge.EVENT_BUS.register(new IconSupplier());
		MinecraftForge.EVENT_BUS.register(new ChargeHud());
		MinecraftForge.EVENT_BUS.register(new VersionCheckerClient());
		MinecraftForge.EVENT_BUS.register(new StackToolTipEvent());
		multiblockRenderEvent = new MultiblockRenderEvent();
		// MinecraftForge.EVENT_BUS.register(multiblockRenderEvent);
		// TODO FIX ME
		ClientRegistry.registerKeyBinding(KeyBindings.config);
		ClientMultiBlocks.init();
	}

	public class RenderManagerNuke implements IRenderFactory<EntityNukePrimed>
	{

		@Override
		public Render<? super EntityNukePrimed> createRenderFor(RenderManager manager)
		{
			return new RenderNukePrimed(manager);
		}
	}

}
