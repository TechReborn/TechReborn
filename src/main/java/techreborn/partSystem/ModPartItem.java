/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.co.qmunity.lib.ref.Names;


public class ModPartItem extends Item {

	ModPart modPart;

	public ModPartItem(ModPart part) {
		modPart = part;
		setUnlocalizedName(Names.Unlocalized.Items.MULTIPART);
	}

	@Override
	public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int face, float x_, float y_, float z_) {
		if(ModPartRegistry.masterProvider != null){
			try {
				if (ModPartRegistry.masterProvider.placePart(item, player, world, x, y, z, face, x_, y_, z_, modPart.getClass().newInstance())) {
					return true;
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return false;
		}else {
			for (IPartProvider partProvider : ModPartRegistry.providers) {
				try {
					if (partProvider.placePart(item, player, world, x, y, z, face, x_, y_, z_, modPart.getClass().newInstance())) {
						return true;
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
