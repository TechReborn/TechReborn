/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ModPartItem extends Item {

    ModPart modPart;

    public ModPartItem(ModPart part) {
        modPart = part;

    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world,
                             int x, int y, int z, int face, float x_, float y_, float z_) {
        ForgeDirection dir = ForgeDirection.getOrientation(face);
        if (ModPartUtils.hasPart(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, modPart.getName())) {
            x = x + dir.offsetX;
            y = y + dir.offsetY;
            z = z + dir.offsetZ;
        }
        if (ModPartUtils.hasPart(world, x, y, z, modPart.getName())) {
            return false;
        }
        if (ModPartRegistry.masterProvider != null) {
            if (ModPartRegistry.masterProvider.placePart(item, player,
                    world, x, y, z, face, x_, y_, z_, (ModPart) modPart.copy())) {
                player.swingItem();
                return true;
            }
        }
        {
            for (IPartProvider partProvider : ModPartRegistry.providers) {
                if (partProvider.placePart(item, player, world, x, y, z, face, x_, y_, z_, (ModPart) modPart.copy())) {
                    player.swingItem();
                    return true;
                }
            }
            return true;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return modPart.getName();
    }

    public ModPart getModPart() {
        return modPart;
    }
}
