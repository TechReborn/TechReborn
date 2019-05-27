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

import net.minecraft.ChatFormat;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.*;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.ItemPowerManager;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.ItemUtils;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRContent;
import techreborn.items.tool.ItemDrill;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemIndustrialDrill extends ItemDrill {

	// 4M FE max charge with 1k charge rate
	public ItemIndustrialDrill() {
		super(ToolMaterials.DIAMOND, ConfigTechReborn.IndustrialDrillCharge, 2.0F, 10F);
		this.cost = 250;
		this.transferLimit = 1000;
	}

	public Set<BlockPos> getTargetBlocks(World worldIn, BlockPos pos, @Nullable PlayerEntity playerIn) {
		Set<BlockPos> targetBlocks = new HashSet<BlockPos>();
		if (!(playerIn instanceof PlayerEntity)) {
			return new HashSet<BlockPos>();
		}
		HitResult raytrace = rayTrace(worldIn, playerIn, false);
		if(raytrace == null || raytrace.sideHit == null){
			return Collections.emptySet();
		}
		Direction enumfacing = raytrace.sideHit;
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

	public void breakBlock(BlockPos pos, World world, PlayerEntity playerIn, ItemStack drill) {
		BlockState blockState = world.getBlockState(pos);

		ItemPowerManager capEnergy = new ItemPowerManager(drill);

		if(capEnergy.getEnergyStored() > cost){
			capEnergy.extractEnergy(cost, false);
			ExternalPowerSystems.requestEnergyFromArmor(capEnergy, playerIn);

			blockState.getBlock().removedByPlayer(blockState, world, pos, playerIn, true, null);
			blockState.getBlock().afterBreak(world, playerIn, pos, blockState, world.getBlockEntity(pos), drill);
			world.removeBlock(pos);
			world.removeBlockEntity(pos);
		}
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
		float originalHardness = worldIn.getBlockState(originalPos).calcBlockBreakingDelta(playerIn, worldIn, originalPos);
		if ((originalHardness / blockHardness) > 10.0F) {
			return false;
		}
		
		return true;	
	}

	// ItemDrill
	@Override
	public boolean onBlockBroken(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		PlayerEntity playerIn = null;
		if ((entityLiving instanceof PlayerEntity)) {
			playerIn = (PlayerEntity) entityLiving;
		}
		if(ItemUtils.isActive(stack)){
			for (BlockPos additionalPos : getTargetBlocks(worldIn, pos, playerIn)) {
				breakBlock(additionalPos, worldIn, playerIn, stack);
			}
		}
		return super.onBlockBroken(stack, worldIn, blockIn, pos, entityLiving);
	}

	@Override
	public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			if (new ItemPowerManager(stack).getEnergyStored() < cost) {
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponent(
					ChatFormat.GRAY + I18n.translate("techreborn.message.nanosaberEnergyErrorTo") + " "
						+ ChatFormat.GOLD + I18n
						.translate("techreborn.message.nanosaberActivate")));
			} else {
				if (!ItemUtils.isActive(stack)) {
					if (stack.getTag() == null) {
						stack.setTag(new CompoundTag());
					}
					stack.getTag().putBoolean("isActive", true);
					if (world.isClient) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponent(
							ChatFormat.GRAY + I18n.translate("techreborn.message.setTo") + " "
								+ ChatFormat.GOLD + I18n
								.translate("techreborn.message.nanosaberActive")));
					}
				} else {
					stack.getTag().putBoolean("isActive", false);
					if (world.isClient) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponent(
							ChatFormat.GRAY + I18n.translate("techreborn.message.setTo") + " "
								+ ChatFormat.GOLD + I18n
								.translate("techreborn.message.nanosaberInactive")));
					}
				}
			}
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		if (ItemUtils.isActive(stack) && new ItemPowerManager(stack).getEnergyStored() < cost) {
			if(entity.world.isClient){
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponent(
					ChatFormat.GRAY + I18n.translate("techreborn.message.nanosaberEnergyError") + " "
						+ ChatFormat.GOLD + I18n
						.translate("techreborn.message.nanosaberDeactivating")));
			}
			stack.getTag().putBoolean("isActive", false);
		}
		return false;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void buildTooltip(ItemStack stack, @Nullable World worldIn, List<Component> tooltip, TooltipContext flagIn) {
		if (!ItemUtils.isActive(stack)) {
			tooltip.add(new TextComponent(ChatFormat.YELLOW + "Shear: " + ChatFormat.RED + I18n.translate("techreborn.message.nanosaberInactive")));
		} else {
			tooltip.add(new TextComponent(ChatFormat.YELLOW + "Shear: " + ChatFormat.GREEN + I18n.translate("techreborn.message.nanosaberActive")));
		}
	}

	// ItemPickaxe
	@Override
	public boolean isEffectiveOn(BlockState blockIn) {
		return (Items.DIAMOND_PICKAXE.isEffectiveOn(blockIn) || Items.DIAMOND_SHOVEL.isEffectiveOn(blockIn)) && !Items.DIAMOND_AXE.isEffectiveOn(blockIn);
	}

	// Item
	@Environment(EnvType.CLIENT)
	@Override
	public void appendItemsForGroup(ItemGroup par2ItemGroup, DefaultedList<ItemStack> itemList) {
		if (!isInItemGroup(par2ItemGroup)) {
			return;
		}
		ItemStack stack = new ItemStack(TRContent.INDUSTRIAL_DRILL);
		ItemStack charged = stack.copy();
		ItemPowerManager capEnergy = new ItemPowerManager(charged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		itemList.add(stack);
		itemList.add(charged);
	}
}
