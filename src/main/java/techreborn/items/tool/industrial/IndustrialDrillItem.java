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

package techreborn.items.tool.industrial;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.Energy;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.items.tool.DrillItem;
import techreborn.utils.InitUtils;
import techreborn.utils.MessageIDs;
import techreborn.utils.ToolsUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IndustrialDrillItem extends DrillItem {

	// 4M FE max charge with 1k charge rate
	public IndustrialDrillItem() {
		super(ToolMaterials.DIAMOND, TechRebornConfig.industrialDrillCharge, 2.0F, 15F);
		this.cost = 250;
		this.transferLimit = 1000;
	}

	private Set<BlockPos> getTargetBlocks(World worldIn, BlockPos pos, @Nullable LivingEntity entityLiving) {
		Set<BlockPos> targetBlocks = new HashSet<>();
		if (!(entityLiving instanceof PlayerEntity) || entityLiving == null) {
			return new HashSet<>();
		}
		PlayerEntity playerIn = (PlayerEntity) entityLiving;

		//Put a dirt block down to raytrace with to stop it raytracing past the intended block
		worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
		HitResult hitResult = rayTrace(worldIn, playerIn, RayTraceContext.FluidHandling.NONE);
		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());

		if(!(hitResult instanceof BlockHitResult)){
			return Collections.emptySet();
		}
		Direction enumfacing = ((BlockHitResult) hitResult).getSide();
		if (enumfacing == Direction.SOUTH || enumfacing == Direction.NORTH) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos newPos = pos.add(i, j, 0);
					if (shouldBreak(playerIn, worldIn, pos, newPos)) {
						targetBlocks.add(newPos);
					}
				}
			}
		} else if (enumfacing == Direction.EAST || enumfacing == Direction.WEST) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos newPos = pos.add(0, j, i);
					if (shouldBreak(playerIn, worldIn, pos, newPos)) {
						targetBlocks.add(newPos);
					}
				}
			}
		} else if (enumfacing == Direction.DOWN || enumfacing == Direction.UP) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos newPos = pos.add(j, 0, i);
					if (shouldBreak(playerIn, worldIn, pos, newPos)) {
						targetBlocks.add(newPos);
					}
				}
			}
		}
		return targetBlocks;
	}

	private boolean shouldBreak(PlayerEntity playerIn, World worldIn, BlockPos originalPos, BlockPos pos) {
		if (originalPos.equals(pos)) {
			return false;
		}
		BlockState blockState = worldIn.getBlockState(pos);
		if (blockState.getMaterial() == Material.AIR) {
			return false;
		}
		if (blockState.getMaterial().isLiquid()) {
			return false;
		}
		float blockHardness = blockState.calcBlockBreakingDelta(playerIn, worldIn, pos);
		if (blockHardness == -1.0F) {
			return false;
		}
		float originalHardness = worldIn.getBlockState(originalPos).getHardness(worldIn, originalPos);
		return !((originalHardness / blockHardness) > 10.0F);
	}

	// DrillItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState stateIn, BlockPos pos, LivingEntity entityLiving) {
		if(ItemUtils.isActive(stack)){
			for (BlockPos additionalPos : getTargetBlocks(worldIn, pos, entityLiving)) {
				ToolsUtil.breakBlock(stack, worldIn, additionalPos, entityLiving, cost);
			}
		}
		return super.postMine(stack, worldIn, stateIn, pos, entityLiving);
	}

	// PickaxeItem
	@Override
	public boolean isEffectiveOn(BlockState blockIn) {
		return (Items.DIAMOND_PICKAXE.isEffectiveOn(blockIn) || Items.DIAMOND_SHOVEL.isEffectiveOn(blockIn)) && !Items.DIAMOND_AXE.isEffectiveOn(blockIn);
	}

	// Item
	@Override
	public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			ItemUtils.switchActive(stack, cost, world.isClient, MessageIDs.poweredToolID);
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public void usageTick(World world, LivingEntity entity,  ItemStack stack, int i) {
		ItemUtils.checkActive(stack, cost, entity.world.isClient, MessageIDs.poweredToolID);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		ItemUtils.buildActiveTooltip(stack, tooltip);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(ItemGroup par2ItemGroup, DefaultedList<ItemStack> itemList) {
		if (!isIn(par2ItemGroup)) {
			return;
		}
		InitUtils.initPoweredItems(TRContent.INDUSTRIAL_DRILL, itemList);
	}
}
