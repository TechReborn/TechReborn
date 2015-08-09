package techreborn.init;

import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;
import techreborn.partSystem.IPartProvider;
import techreborn.partSystem.ModPartRegistry;
import techreborn.partSystem.parts.CablePart;
import techreborn.partSystem.parts.FarmInventoryCable;

import java.util.HashMap;

public class ModParts {

    public static HashMap<Integer, ItemStack> stackCable = new HashMap<Integer, ItemStack>();

    public static void init() {
        if (Loader.isModLoaded("IC2")) {
            for (int i = 0; i < 13; i++) {
                CablePart part = new CablePart();
                part.setType(i);
                ModPartRegistry.registerPart(part);
            }
        }
        ModPartRegistry.registerPart(new FarmInventoryCable());
        ModPartRegistry.addProvider("techreborn.partSystem.fmp.FMPFactory",
                "ForgeMultipart");
        ModPartRegistry.addProvider("techreborn.partSystem.QLib.QModPartFactory", "qmunitylib");
        ModPartRegistry.addAllPartsToSystems();
        for (IPartProvider provider : ModPartRegistry.providers) {
            if (provider.modID().equals("ForgeMultipart")) {
                ModPartRegistry.masterProvider = provider;
            }
        }
    }
}
