package techreborn.parts.fluidPipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import reborncore.RebornCore;
import reborncore.mcmultipart.item.ItemMultiPart;
import reborncore.mcmultipart.multipart.IMultipart;
import techreborn.client.TechRebornCreativeTab;

/**
 * Created by modmuss50 on 09/05/2016.
 */
public class ItemFluidPipe extends ItemMultiPart {

	public ItemFluidPipe() {
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.fluidpipe");
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
		return new EmptyFluidPipe();
	}
}
