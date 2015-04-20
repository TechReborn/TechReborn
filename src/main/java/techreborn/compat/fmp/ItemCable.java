package techreborn.compat.fmp;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCable extends JItemMultiPart {
	@Override
	public TMultiPart newPart(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockCoord blockCoord, int i, Vector3 vector3) {
		return MultiPartRegistry.createPart("TRCable", false);
	}
}
