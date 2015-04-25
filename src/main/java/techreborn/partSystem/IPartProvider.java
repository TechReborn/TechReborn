/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.lib.Location;
import techreborn.lib.vecmath.Vecs3dCube;

public interface IPartProvider {

    public String modID();

    public void registerPart();

    public boolean checkOcclusion(World world, Location location,
                                  Vecs3dCube cube);

    public boolean hasPart(World world, Location location, String name);

    public boolean placePart(ItemStack item, EntityPlayer player, World world,
                             int x, int y, int z, int side, float hitX, float hitY, float hitZ,
                             ModPart modPart);

    public boolean isTileFromProvider(TileEntity tileEntity);

	public IModPart getPartFromWorld(World world, Location location, String name);
}
