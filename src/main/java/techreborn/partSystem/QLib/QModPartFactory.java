/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.QLib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.lib.Location;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.IModPart;
import techreborn.partSystem.IPartProvider;
import techreborn.partSystem.ModPart;
import techreborn.partSystem.ModPartRegistry;
import techreborn.partSystem.parts.CablePart;
import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartFactory;
import uk.co.qmunity.lib.part.PartRegistry;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import java.lang.reflect.InvocationTargetException;

public class QModPartFactory implements IPartFactory, IPartProvider {
    @Override
    public IPart createPart(String type, boolean client) {
        for (ModPart modPart : ModPartRegistry.parts) {
            if (modPart.getName().equals(type)) {
                try {
                    if(modPart instanceof CablePart){
                        try {
                            return new QModPart(modPart.getClass().getDeclaredConstructor(int.class).newInstance(((CablePart) modPart).type));
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    return new QModPart(modPart.getClass().newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean placePart(ItemStack item, EntityPlayer player, World world,
                             int x, int y, int z, int face, float x_, float y_, float z_,
                             ModPart modPart) {
        IPart part = createPart(
                item,
                player,
                world,
                new MovingObjectPosition(x, y, z, face, Vec3
                        .createVectorHelper(x + x_, y + y_, z + z_)), modPart);

        if (part == null)
            return false;

        ForgeDirection dir = ForgeDirection.getOrientation(face);
        return MultipartCompatibility.placePartInWorld(part, world, new Vec3i(
                x, y, z), dir, player, item);
    }

    @Override
    public boolean isTileFromProvider(TileEntity tileEntity) {
        return tileEntity instanceof TileMultipart;
    }

	@Override
	public IModPart getPartFromWorld(World world, Location location, String name) {
		IPart part = MultipartCompatibility.getPart(world, location.getX(), location.getY(), location.getZ(), name);
		if(part != null){
			if(part instanceof QModPart){
				return ((QModPart) part).iModPart;
			}
		}
		return null;
	}

	public String getCreatedPartType(ItemStack item, EntityPlayer player,
                                     World world, MovingObjectPosition mop, ModPart modPart) {
        return modPart.getName();
    }

    public IPart createPart(ItemStack item, EntityPlayer player, World world,
                            MovingObjectPosition mop, ModPart modPart) {

        return PartRegistry.createPart(
                getCreatedPartType(item, player, world, mop, modPart),
                world.isRemote);
    }

    @Override
    public String modID() {
        return QLModInfo.MODID;
    }

    @Override
    public void registerPart() {
        PartRegistry.registerFactory(new QModPartFactory());
    }

    @Override
    public boolean checkOcclusion(World world, Location location,
                                  Vecs3dCube cube) {
        return MultipartCompatibility.checkOcclusion(world, location.x,
                location.y, location.z, new Vec3dCube(cube.toAABB()));
    }

    @Override
    public boolean hasPart(World world, Location location, String name) {
        return MultipartCompatibility.getPart(world, location.getX(), location.getY(), location.getZ(), name) == null;
    }
}
