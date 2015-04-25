/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem;

import net.minecraft.item.Item;
import net.minecraft.world.World;
import techreborn.lib.Location;
import techreborn.lib.vecmath.Vecs3dCube;

import java.util.Map;

public class ModPartUtils {

    public static boolean checkOcclusion(World world, Location location,
                                         Vecs3dCube cube) {
        if (world == null) {
            return false;
        }
        IPartProvider partProvider = getPartProvider(world, location);
        if (partProvider != null) {
            return partProvider.checkOcclusion(world, location, cube);
        }
        return false;
    }

    public static boolean checkOcclusion(World world, int x, int y, int z, Vecs3dCube cube) {
        return !checkOcclusion(world, new Location(x, y, z), cube);
    }

    public static boolean checkOcclusionInvert(World world, Location location,
                                               Vecs3dCube cube) {
        if (world == null) {
            return false;
        }
        for (IPartProvider iPartProvider : ModPartRegistry.providers) {
            if (!iPartProvider.checkOcclusion(world, location, cube)) {
                return false;
            }
        }
        return false;
    }

    public static boolean checkOcclusionInvert(World world, int x, int y,
                                               int z, Vecs3dCube cube) {
        return checkOcclusionInvert(world, new Location(x, y, z), cube);
    }

    public static boolean hasPart(World world, Location location, String name) {
        for (IPartProvider iPartProvider : ModPartRegistry.providers) {
            if (iPartProvider.hasPart(world, location, name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPart(World world, int x, int y, int z, String name) {
        return hasPart(world, new Location(x, y, z), name);
    }

    public static Item getItemForPart(String string) {
        for (Map.Entry<Item, String> item : ModPartRegistry.itemParts
                .entrySet()) {
            if (item.getValue().equals(string)) {
                return item.getKey();
            }
        }
        return null;
    }

    public static IPartProvider getPartProvider(World world, Location location) {
        for (IPartProvider partProvider : ModPartRegistry.providers) {
            if (partProvider.isTileFromProvider(world.getTileEntity(
                    location.getX(), location.getY(), location.getZ()))) {
                return partProvider;
            }
        }
        return null;
    }

	public static IModPart getPartFromWorld(World world, Location location, String name){
		for (IPartProvider partProvider : ModPartRegistry.providers) {
			IModPart tempPart = null;
			tempPart = partProvider.getPartFromWorld(world, location, name);
			if(tempPart != null){
				return tempPart;
			}
		}
		return null;
	}
}
