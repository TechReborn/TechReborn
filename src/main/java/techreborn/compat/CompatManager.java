package techreborn.compat;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import techreborn.client.render.parts.ClientPartLoader;
import techreborn.compat.ic2.RecipesIC2;
import techreborn.compat.minetweaker.MinetweakerCompat;
import techreborn.compat.theoneprobe.CompactTheOneProbe;
import techreborn.compat.tinkers.CompatModuleTinkers;
import techreborn.compat.waila.CompatModuleWaila;
import techreborn.config.ConfigTechReborn;
import techreborn.parts.StandalonePartCompact;
import techreborn.parts.TechRebornParts;
import techreborn.parts.walia.WailaMcMultiPartCompact;

import java.util.ArrayList;

public class CompatManager {

	public static CompatManager INSTANCE = new CompatManager();
	public static boolean isIC2Loaded = false;
	public static boolean isIC2ClassicLoaded = false;
	public static boolean isQuantumStorageLoaded = false;
	public ArrayList<ICompatModule> compatModules = new ArrayList<>();

	public CompatManager() {
		isIC2Loaded = Loader.isModLoaded("IC2");
		isIC2ClassicLoaded = Loader.isModLoaded("IC2-Classic-Spmod");
		isQuantumStorageLoaded = Loader.isModLoaded("quantumstorage");
		register(MinetweakerCompat.class, "MineTweaker3");
		registerCompact(TechRebornParts.class, false, "reborncore-mcmultipart");
		registerCompact(ClientPartLoader.class, false, "reborncore-mcmultipart", "@client");
		registerCompact(StandalonePartCompact.class, false, "!reborncore-mcmultipart");
		registerCompact(WailaMcMultiPartCompact.class, false, "reborncore-mcmultipart", "Waila", "!IC2");
		register(CompatModuleWaila.class, "Waila");
		register(CompatModuleTinkers.class, "tconstruct");
		register(CompactTheOneProbe.class, "theoneprobe");
		//register(CompatModulePsi.class, "Psi");
		register(RecipesIC2.class, "IC2");
	}

	public void register(Class<? extends ICompatModule> moduleClass, Object... objs) {
		registerCompact(moduleClass, true, objs);
	}

	public void registerCompact(Class<? extends ICompatModule> moduleClass, boolean config, Object... objs) {
		boolean shouldLoad = true;
		if (config) {
			shouldLoad = ConfigTechReborn.config
				.get(ConfigTechReborn.CATEGORY_INTEGRATION, "Compat:" + moduleClass.getSimpleName(), true,
					"Should the " + moduleClass.getSimpleName() + " be loaded?")
				.getBoolean(true);
		}
		for (Object obj : objs) {
			if (obj instanceof String) {
				String modid = (String) obj;
				if (modid.startsWith("@")) {
					if (modid.equals("@client")) {
						if (FMLCommonHandler.instance().getSide() != Side.CLIENT) {
							return;
						}
					}
				} else if (modid.startsWith("!")) {
					if (Loader.isModLoaded(modid.replaceAll("!", ""))) {
						return;
					}
				} else {
					if (!Loader.isModLoaded(modid)) {
						return;
					}
				}
			} else if (obj instanceof Boolean) {
				Boolean boo = (Boolean) obj;
				if (!boo) {
				}
				return;
			}
		}
		if (config) {
			if (ConfigTechReborn.config.hasChanged())
				ConfigTechReborn.config.save();
			if (!shouldLoad) {
				return;
			}
		}
		try {
			compatModules.add(moduleClass.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
