/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.fmp;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import reborncore.common.misc.Location;
import reborncore.common.misc.vecmath.Vecs3dCube;
import techreborn.partSystem.IModPart;
import techreborn.partSystem.IPartProvider;
import techreborn.partSystem.ModPart;
import techreborn.partSystem.ModPartRegistry;

import java.util.List;

public class FMPFactory implements MultiPartRegistry.IPartFactory2,
        IPartProvider {


    public TMultiPart createPart(String type, boolean client) {
        for (ModPart modPart : ModPartRegistry.parts) {
            if (modPart.getName().equals(type)) {
                return new FMPModPart((ModPart) modPart.copy());
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

    @Override
    public IModPart getPartFromWorld(World world, Location location, String name) {
        TileEntity tileEntity = world.getTileEntity(location.getX(),
                location.getY(), location.getZ());
        if (tileEntity instanceof TileMultipart) {
            TileMultipart mp = (TileMultipart) tileEntity;
            boolean ret = false;
            List<TMultiPart> t = mp.jPartList();
            for (TMultiPart p : t) {
                if (ret == false) {
                    if (p.getType().equals(name)) {
                        if (p instanceof FMPModPart) {
                            return ((FMPModPart) p).iModPart;
                        }
                        ret = true;
                    }
                }
            }
            return null;
        }
        return null;
    }


    @Override
    public void init() {
        if (Loader.isModLoaded("IC2")) {
            MultiPartRegistry.registerConverter(new CableConverter());
            MinecraftForge.EVENT_BUS.register(new CableConverter());
        }
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

    @Override
    public TMultiPart createPart(String s, NBTTagCompound nbtTagCompound) {
        return createPart(s, false);
    }

    @Override
    public TMultiPart createPart(String s, MCDataInput mcDataInput) {
        return createPart(s, false);
    }
}
