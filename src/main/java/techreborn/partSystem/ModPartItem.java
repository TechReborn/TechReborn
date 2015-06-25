/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.partSystem.parts.CablePart;
import uk.co.qmunity.lib.ref.Names;

public class ModPartItem extends Item {

    ModPart modPart;

    public ModPartItem(ModPart part) {
        modPart = part;
        setUnlocalizedName(Names.Unlocalized.Items.MULTIPART);
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world,
                             int x, int y, int z, int face, float x_, float y_, float z_) {
        ForgeDirection dir = ForgeDirection.getOrientation(face);
        if(ModPartUtils.hasPart(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, modPart.getName())){
            x = x + dir.offsetX;
            y = y + dir.offsetY;
            z = z + dir.offsetZ;
        }
        if(ModPartUtils.hasPart(world, x, y, z, modPart.getName())){
            return false;
        }
        if (ModPartRegistry.masterProvider != null) {
            try {
				if(modPart instanceof CablePart){
					if (ModPartRegistry.masterProvider.placePart(item, player, world, x, y, z, face, x_, y_, z_, modPart.getClass().getDeclaredConstructor(int.class).newInstance(((CablePart) modPart).type))) {
						return true;
					}
				}else{
					if (ModPartRegistry.masterProvider.placePart(item, player,
							world, x, y, z, face, x_, y_, z_, modPart.getClass()
									.newInstance())) {
						return true;
					}
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
		{
            for (IPartProvider partProvider : ModPartRegistry.providers) {
                try {
                    if(modPart instanceof CablePart){
                        try {
                            if (partProvider.placePart(item, player, world, x, y, z, face, x_, y_, z_, modPart.getClass().getDeclaredConstructor(int.class).newInstance(((CablePart) modPart).type))) {
                                return true;
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (partProvider.placePart(item, player, world, x, y, z, face, x_, y_, z_, modPart.getClass().newInstance())) {
                            return true;
                        }
                    }

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
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
