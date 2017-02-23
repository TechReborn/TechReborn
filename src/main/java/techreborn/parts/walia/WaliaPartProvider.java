/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
		return ItemStack.EMPTY;
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
