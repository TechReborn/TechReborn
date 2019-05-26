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
import net.minecraft.tag.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.ItemUtils;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRContent;
import techreborn.items.tool.ItemChainsaw;
import techreborn.utils.MessageIDs;
import techreborn.utils.TagUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemIndustrialChainsaw extends ItemChainsaw {

	private static final Direction[] SEARCH_ORDER = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP};


	// 4M FE max charge with 1k charge rate
	public ItemIndustrialChainsaw() {
		super(ToolMaterials.DIAMOND, ConfigTechReborn.IndustrialChainsawCharge, 1.0F);
		this.cost = 250;
		this.transferLimit = 1000;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendItemsForGroup(ItemGroup par2ItemGroup, DefaultedList<ItemStack> itemList) {
		if (!isInItemGroup(par2ItemGroup)) {
			return;
		}
		ItemStack stack = new ItemStack(TRContent.INDUSTRIAL_CHAINSAW);
		ItemStack charged = stack.copy();
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(charged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		itemList.add(stack);
		itemList.add(charged);
	}

	@Override
	public boolean onBlockBroken(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		if(ItemUtils.isActive(stack) && !worldIn.isClient){
			BlockState state = worldIn.getBlockState(pos);
			if(state.getBlock() instanceof IShearable){
				if(((IShearable) state.getBlock()).isShearable(stack, worldIn, pos)){
					List<ItemStack> results = ((IShearable) state.getBlock()).onSheared(stack, worldIn, pos, 0);
					results.forEach(itemStack -> ItemScatterer.spawn(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), itemStack));
					return super.onBlockBroken(stack, worldIn, blockIn, pos, entityLiving);
				}
			}
		}

		List<BlockPos> wood = new ArrayList<>();
		findWood(worldIn, pos, wood, new ArrayList<>());
		wood.forEach(pos1 -> breakBlock(pos1, stack, worldIn, entityLiving, pos));
		return super.onBlockBroken(stack, worldIn, blockIn, pos, entityLiving);
	}

	private void findWood(World world, BlockPos pos, List<BlockPos> wood, List<BlockPos> leaves){
		//Limit the amount of wood to be broken to 64 blocks.
		if(wood.size() >= 64){
			return;
		}
		//Search 150 leaves for wood
		if(leaves.size() >= 150){
			return;
		}
		for(Direction facing : SEARCH_ORDER){
			BlockPos checkPos = pos.offset(facing);
			if(!wood.contains(checkPos) && !leaves.contains(checkPos)){
				BlockState state = world.getBlockState(checkPos);
				if(TagUtils.hasTag(state.getBlock(), BlockTags.LOGS)){
					wood.add(checkPos);
					findWood(world, checkPos, wood, leaves);
				} else if(TagUtils.hasTag(state.getBlock(), BlockTags.LEAVES)){
					leaves.add(checkPos);
					findWood(world, checkPos, wood, leaves);
				}
			}

		}
	}

	@Override
	public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			if (new ForgePowerItemManager(stack).getEnergyStored() < cost) {
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
		if (ItemUtils.isActive(stack) && new ForgePowerItemManager(stack).getEnergyStored() < cost) {
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

	@Override
	public boolean isEffectiveOn(BlockState blockIn) {
		return Items.DIAMOND_AXE.isEffectiveOn(blockIn);
	}

	public void breakBlock(BlockPos pos, ItemStack stack, World world, LivingEntity entityLiving, BlockPos oldPos) {
		if (oldPos == pos) {
			return;
		}

		ForgePowerItemManager capEnergy = new ForgePowerItemManager(stack);
		if (capEnergy.getEnergyStored() < cost) {
			return;
		}

		BlockState blockState = world.getBlockState(pos);
		if (blockState.getHardness(world, pos) == -1.0F) {
			return;
		}
		if(!(entityLiving instanceof PlayerEntity)){
			return;
		}

		capEnergy.extractEnergy(cost, false);
		ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityLiving);

		blockState.getBlock().afterBreak(world, (PlayerEntity) entityLiving, pos, blockState, world.getBlockEntity(pos), stack);
		world.removeBlock(pos);
		world.removeBlockEntity(pos);
	}
}
