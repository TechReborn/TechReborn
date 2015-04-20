/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package me.modmuss50.network.partSystem.QLib;

import me.modmuss50.mods.lib.Location;
import me.modmuss50.mods.lib.vecmath.Vecs3dCube;
import me.modmuss50.network.partSystem.IModPart;
import me.modmuss50.network.partSystem.IPartProvider;
import me.modmuss50.network.partSystem.ModPart;
import me.modmuss50.network.partSystem.ModPartRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartFactory;
import uk.co.qmunity.lib.part.PartRegistry;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import java.util.List;


public class QModPartFactory implements IPartFactory, IPartProvider {
	@Override
	public IPart createPart(String type, boolean client) {
		for (ModPart modPart : ModPartRegistry.parts) {
			if (modPart.getName().equals(type)) {
				try {
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
	public boolean placePart(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int face, float x_, float y_, float z_, ModPart modPart) {
		IPart part = createPart(item, player, world,
				new MovingObjectPosition(x, y, z, face, Vec3.createVectorHelper(x + x_, y + y_, z + z_)), modPart);

		if (part == null)
			return false;

		ForgeDirection dir = ForgeDirection.getOrientation(face);
		return MultipartCompatibility.placePartInWorld(part, world, new Vec3i(x, y, z), dir, player, item);
	}

	@Override
	public boolean isTileFromProvider(TileEntity tileEntity) {
		return tileEntity instanceof TileMultipart;
	}

	public String getCreatedPartType(ItemStack item, EntityPlayer player, World world, MovingObjectPosition mop, ModPart modPart) {
		return modPart.getName();
	}

	public IPart createPart(ItemStack item, EntityPlayer player, World world, MovingObjectPosition mop, ModPart modPart) {

		return PartRegistry.createPart(getCreatedPartType(item, player, world, mop, modPart), world.isRemote);
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
	public boolean checkOcclusion(World world, Location location, Vecs3dCube cube) {
		return MultipartCompatibility.checkOcclusion(world, location.x, location.y, location.z, new Vec3dCube(cube.toAABB()));
	}

	@Override
	public boolean hasPart(World world, Location location, String name) {
		TileEntity tileEntity = world.getTileEntity(location.getX(), location.getY(), location.getZ());
		if (tileEntity instanceof TileMultipart) {
			TileMultipart mp = (TileMultipart) tileEntity;
			boolean ret = false;
			List<IPart> t = mp.getParts();
			for (IPart p : t) {
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
