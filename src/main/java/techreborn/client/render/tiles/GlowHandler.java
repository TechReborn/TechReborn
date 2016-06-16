package techreborn.client.render.tiles;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.common.tile.TileMachineBase;
import techreborn.proxies.ClientProxy;
import techreborn.tiles.TileAssemblingMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mark on 15/06/2016.
 */
public class GlowHandler {

	public HashMap<Class<? extends TileMachineBase>, List<GlowInformation>> informationHashMap = new HashMap<>();

	private TESRGlowing tesrGlowing = null;

	public void register(Class<? extends TileMachineBase> te, GlowInformation... glowInformations){
		List<GlowInformation> list = new ArrayList<>();
		for(GlowInformation glowInformation : glowInformations){
			list.add(glowInformation);
		}
		informationHashMap.put(te, list);
		if(tesrGlowing == null){
			tesrGlowing = new TESRGlowing();
		}
		ClientRegistry.bindTileEntitySpecialRenderer(te, tesrGlowing);
	}

	public void load(){
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.post(new GlowRegisterEvent(this));
	}

	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre e) {
		for(Map.Entry<Class<? extends TileMachineBase>, List<GlowInformation>> glowSet : informationHashMap.entrySet()){
			for(GlowInformation information : glowSet.getValue()){
				information.setTextureAtlasSprite(e.getMap().registerSprite(information.getTextureLocation()));
			}
		}
	}

	@SubscribeEvent
	public void registerGlow(GlowRegisterEvent e){
		e.handler.register(TileAssemblingMachine.class, new GlowInformation(null, new ResourceLocation("techreborn", "blocks/machines/tier1_machines/electric_alloy_smelter_front_on_glow")));
	}

	public class GlowRegisterEvent extends Event {

		GlowHandler handler;


		public GlowRegisterEvent(GlowHandler handler) {
			this.handler = handler;
		}

		public GlowHandler getHandler() {
			return handler;
		}
	}

	public static GlowHandler createAndLoad(){
		ClientProxy.handler = new GlowHandler();
		ClientProxy.handler.load();
		return ClientProxy.handler;
	}


}
