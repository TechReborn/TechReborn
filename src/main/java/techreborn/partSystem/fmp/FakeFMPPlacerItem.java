/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package techreborn.partSystem.fmp;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import techreborn.partSystem.ModPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This item is never added into the game, it is only used to add the part to the world.
 */
public class FakeFMPPlacerItem extends JItemMultiPart {
	ModPart modPart;

	public FakeFMPPlacerItem(ModPart part) {
		modPart = part;
	}

	@Override
	public TMultiPart newPart(ItemStack item, EntityPlayer player, World world, BlockCoord pos, int side, Vector3 vhit) {
		TMultiPart w = MultiPartRegistry.createPart(modPart.getName(), false);
		return w;
	}
}
