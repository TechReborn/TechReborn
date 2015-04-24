/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import techreborn.client.TechRebornCreativeTab;
import techreborn.util.LogHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModPartRegistry {

    public static ArrayList<ModPart> parts = new ArrayList<ModPart>();

    public static ArrayList<IPartProvider> providers = new ArrayList<IPartProvider>();

    public static IPartProvider masterProvider = null;

    public static Map<Item, String> itemParts = new HashMap<Item, String>();

    public static void registerPart(ModPart iModPart) {
        parts.add(iModPart);
    }

    public static void addAllPartsToSystems() {
        LogHelper.info("Started to load all parts");

        for (ModPart modPart : ModPartRegistry.parts) {
            System.out.println(modPart.getName());
            Item part = new ModPartItem(modPart)
                    .setUnlocalizedName(modPart.getName())
                    .setCreativeTab(TechRebornCreativeTab.instance)
                    .setTextureName(modPart.getItemTextureName());
            GameRegistry.registerItem(part, modPart.getName());
            itemParts.put(part, modPart.getName());
        }

        for (IPartProvider iPartProvider : providers) {
            iPartProvider.registerPart();
        }
    }

    public static Item getItem(String string) {
        for (Map.Entry<Item, String> entry : itemParts.entrySet()) {
            if (entry.getValue().equals(string)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void addProvider(String className, String modid) {
        if (Loader.isModLoaded(modid) || modid.equals("Minecraft")) {
            try {
                IPartProvider iPartProvider = null;
                iPartProvider = (IPartProvider) Class.forName(className)
                        .newInstance();
                providers.add(iPartProvider);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                LogHelper.error("Failed to load " + className
                        + " to the part system!");
            } catch (InstantiationException e) {
                e.printStackTrace();
                LogHelper.error("Failed to load " + className
                        + " to the part system!");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                LogHelper.error("Failed to load " + className
                        + " to the part system!");
            }
        }
    }

    // Only use this one if it is a standalone Provider
    public static void addProvider(IPartProvider iPartProvider) {
        if (Loader.isModLoaded(iPartProvider.modID())
                || iPartProvider.modID().equals("Minecraft")) {
            providers.add(iPartProvider);
        }
    }

}
