package techreborn.parts.walia;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.mcmultipart.block.TileMultipartContainer;
import reborncore.mcmultipart.raytrace.PartMOP;
import reborncore.mcmultipart.raytrace.RayTraceUtils;
import techreborn.parts.powerCables.CableMultipart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 07/03/2016.
 */
public class WaliaPartProvider implements IWailaDataProvider {
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		PartMOP mop = reTrace(accessor.getWorld(), accessor.getPosition(), accessor.getPlayer());
		if (mop != null) {
			if (mop.partHit instanceof CableMultipart) {
				return mop.partHit.getDrops().get(0);
			}
		}
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
	                                 IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
	                                 IWailaConfigHandler config) {
		PartMOP mop = reTrace(accessor.getWorld(), accessor.getPosition(), accessor.getPlayer());
		List<String> data = new ArrayList<>();
		if (mop != null) {
			if (mop.partHit instanceof IPartWaliaProvider) {
				((IPartWaliaProvider) mop.partHit).addInfo(data);
			}
		}
		return data;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
	                                 IWailaConfigHandler config) {
		return null;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
	                                 BlockPos pos) {
		return null;
	}

	// Stolen from
	// https://github.com/amadornes/MCMultiPart/blob/master/src/main/java/mcmultipart/block/BlockMultipart.java
	private PartMOP reTrace(World world, BlockPos pos, EntityPlayer player) {
		Vec3d start = RayTraceUtils.getStart(player);
		Vec3d end = RayTraceUtils.getEnd(player);
		RayTraceUtils.AdvancedRayTraceResultPart result = getMultipartTile(world, pos).getPartContainer()
			.collisionRayTrace(start, end);
		return result == null ? null : result.hit;
	}

	// Stolen from
	// https://github.com/amadornes/MCMultiPart/blob/master/src/main/java/mcmultipart/block/BlockMultipart.java
	private TileMultipartContainer getMultipartTile(IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileMultipartContainer ? (TileMultipartContainer) tile : null;
	}
}
