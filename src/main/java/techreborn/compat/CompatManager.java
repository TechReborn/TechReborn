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
	public static boolean isQuantumStorageLoaded = false;
	public ArrayList<ICompatModule> compatModules = new ArrayList<>();

	public CompatManager() {
		isIC2Loaded = Loader.isModLoaded("IC2");
		isQuantumStorageLoaded = Loader.isModLoaded("quantumstorage");
		registerCompact(MinetweakerCompat.class, "MineTweaker3");
		registerCompact(TechRebornParts.class, "reborncore-mcmultipart");
		registerCompact(ClientPartLoader.class, "reborncore-mcmultipart", "@client");
		registerCompact(StandalonePartCompact.class, "!reborncore-mcmultipart");
		registerCompact(WailaMcMultiPartCompact.class, "reborncore-mcmultipart", "Waila", "!IC2");
		registerCompact(CompatModuleWaila.class, "Waila");
		registerCompact(CompatModuleTinkers.class, "tconstruct");
		registerCompact(CompactTheOneProbe.class, "theoneprobe");
		//registerCompact(CompatModulePsi.class, "Psi");
		registerCompact(RecipesIC2.class, "IC2");
	}

	public void registerCompact(Class<? extends ICompatModule> moduleClass, Object... objs) {
		boolean shouldLoad = ConfigTechReborn.config
			.get(ConfigTechReborn.CATEGORY_INTEGRATION, "Compat:" + moduleClass.getSimpleName(), true,
				"Should the " + moduleClass.getSimpleName() + " be loaded?")
			.getBoolean(true);
		if (ConfigTechReborn.config.hasChanged())
			ConfigTechReborn.config.save();
		if (!shouldLoad) {
			return;
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
		try {
			compatModules.add(moduleClass.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
