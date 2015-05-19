/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.fmp;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.lib.Location;
import techreborn.lib.vecmath.Vecs3dCube;
import techreborn.partSystem.IModPart;
import techreborn.partSystem.IPartProvider;
import techreborn.partSystem.ModPart;
import techreborn.partSystem.ModPartRegistry;
import techreborn.partSystem.parts.CablePart;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

/**
 * Created by mark on 09/12/14.
 */
public class FMPFactory implements MultiPartRegistry.IPartFactory,
        IPartProvider {
    @Override
    public TMultiPart createPart(String type, boolean client) {
        for (ModPart modPart : ModPartRegistry.parts) {
            if (modPart.getName().equals(type)) {
                try {
                    if(modPart instanceof CablePart){
                        return new FMPModPart(modPart.getClass().getDeclaredConstructor(int.class).newInstance(((CablePart) modPart).type));
                    } else {
                        return new FMPModPart(modPart.getClass().newInstance());
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean placePart(ItemStack item, EntityPlayer player, World world,
                             int x, int y, int z, int side, float hitX, float hitY, float hitZ,
                             ModPart modPart) {
        return new FakeFMPPlacerItem(modPart).onItemUse(item, player, world, x,
                y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean isTileFromProvider(TileEntity tileEntity) {
        return tileEntity instanceof TileMultipart;
    }

	//TODO
	@Override
	public IModPart getPartFromWorld(World world, Location location, String name) {
		return null;
	}

	@Override
    public String modID() {
        return "ForgeMultipart";
    }

    @Override
    public void registerPart() {
        for (ModPart modPart : ModPartRegistry.parts) {
            MultiPartRegistry.registerParts(new FMPFactory(), new String[]
                    {modPart.getName()});
        }
    }

    @Override
    public boolean checkOcclusion(World world, Location location,
                                  Vecs3dCube cube) {
        codechicken.multipart.TileMultipart tmp = codechicken.multipart.TileMultipart
                .getOrConvertTile(world, new BlockCoord(location.getX(),
                        location.getY(), location.getZ()));
        if (tmp == null)
            return false;
        return !tmp.occlusionTest(tmp.partList(), new NormallyOccludedPart(
                new Cuboid6(cube.toAABB())));
    }

    @Override
    public boolean hasPart(World world, Location location, String name) {
        TileEntity tileEntity = world.getTileEntity(location.getX(),
                location.getY(), location.getZ());
        if (tileEntity instanceof TileMultipart) {
            TileMultipart mp = (TileMultipart) tileEntity;
            boolean ret = false;
            List<TMultiPart> t = mp.jPartList();
            for (TMultiPart p : t) {
                if (ret == false) {
                    if (p.getType().equals(name)) {
                        ret = true;
                    }
                }
            }
            return ret;
        }
        return false;
    }
}
