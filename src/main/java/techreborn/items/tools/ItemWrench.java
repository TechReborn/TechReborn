/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.context.BlockPosContext;
import reborncore.api.IToolHandler;
import reborncore.common.util.RebornPermissions;
import techreborn.items.ItemTR;

/**
 * Created by modmuss50 on 26/02/2016.
 */
public class ItemWrench extends ItemTR implements IToolHandler {

	//Set by TR mod compat
	public static WrenchContext ic2WrenchContext;

	public ItemWrench() {
		setTranslationKey("techreborn.wrench");
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
	                                  EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(ic2WrenchContext != null){
			EnumActionResult actionResult = ic2WrenchContext.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
			if(actionResult != EnumActionResult.FAIL){
				return actionResult;
			}
		}
		if (!world.isRemote && !PermissionAPI.hasPermission(player.getGameProfile(), RebornPermissions.WRENCH_BLOCK, new BlockPosContext(player, pos, world.getBlockState(pos), facing))) {
			return EnumActionResult.PASS;
		}
		return EnumActionResult.PASS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean handleTool(ItemStack stack, BlockPos pos, World world, EntityPlayer player, EnumFacing side, boolean damage) {
		if(damage){
			stack.damageItem(1, player);
		}
		return true;
	}

	@FunctionalInterface
	public interface WrenchContext {
		EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
		          EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);
	}
}
