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

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;
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

	private static final EnumFacing[] SEARCH_ORDER = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.UP};


	// 4M FE max charge with 1k charge rate
	public ItemIndustrialChainsaw() {
		super(ItemTier.DIAMOND, ConfigTechReborn.IndustrialChainsawCharge, 1.0F);
		this.cost = 250;
		this.transferLimit = 1000;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void fillItemGroup(ItemGroup par2ItemGroup, NonNullList<ItemStack> itemList) {
		if (!isInGroup(par2ItemGroup)) {
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
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
		if(ItemUtils.isActive(stack) && !worldIn.isRemote){
			IBlockState state = worldIn.getBlockState(pos);
			if(state.getBlock() instanceof IShearable){
				if(((IShearable) state.getBlock()).isShearable(stack, worldIn, pos)){
					List<ItemStack> results = ((IShearable) state.getBlock()).onSheared(stack, worldIn, pos, 0);
					results.forEach(itemStack -> InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), itemStack));
					return super.onBlockDestroyed(stack, worldIn, blockIn, pos, entityLiving);
				}
			}
		}

		List<BlockPos> wood = new ArrayList<>();
		findWood(worldIn, pos, wood, new ArrayList<>());
		wood.forEach(pos1 -> breakBlock(pos1, stack, worldIn, entityLiving, pos));
		return super.onBlockDestroyed(stack, worldIn, blockIn, pos, entityLiving);
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
		for(EnumFacing facing : SEARCH_ORDER){
			BlockPos checkPos = pos.offset(facing);
			if(!wood.contains(checkPos) && !leaves.contains(checkPos)){
				IBlockState state = world.getBlockState(checkPos);
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
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
		final ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			if (new ForgePowerItemManager(stack).getEnergyStored() < cost) {
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
					TextFormatting.GRAY + I18n.format("techreborn.message.nanosaberEnergyErrorTo") + " "
						+ TextFormatting.GOLD + I18n
						.format("techreborn.message.nanosaberActivate")));
			} else {
				if (!ItemUtils.isActive(stack)) {
					if (stack.getTag() == null) {
						stack.setTag(new NBTTagCompound());
					}
					stack.getTag().putBoolean("isActive", true);
					if (world.isRemote) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
							TextFormatting.GRAY + I18n.format("techreborn.message.setTo") + " "
								+ TextFormatting.GOLD + I18n
								.format("techreborn.message.nanosaberActive")));
					}
				} else {
					stack.getTag().putBoolean("isActive", false);
					if (world.isRemote) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
							TextFormatting.GRAY + I18n.format("techreborn.message.setTo") + " "
								+ TextFormatting.GOLD + I18n
								.format("techreborn.message.nanosaberInactive")));
					}
				}
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, EntityItem entity) {
		if (ItemUtils.isActive(stack) && new ForgePowerItemManager(stack).getEnergyStored() < cost) {
			if(entity.world.isRemote){
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
					TextFormatting.GRAY + I18n.format("techreborn.message.nanosaberEnergyError") + " "
						+ TextFormatting.GOLD + I18n
						.format("techreborn.message.nanosaberDeactivating")));
			}
			stack.getTag().putBoolean("isActive", false);
		}
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (!ItemUtils.isActive(stack)) {
			tooltip.add(new TextComponentString(TextFormatting.YELLOW + "Shear: " + TextFormatting.RED + I18n.format("techreborn.message.nanosaberInactive")));
		} else {
			tooltip.add(new TextComponentString(TextFormatting.YELLOW + "Shear: " + TextFormatting.GREEN + I18n.format("techreborn.message.nanosaberActive")));
		}
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return Items.DIAMOND_AXE.canHarvestBlock(blockIn);
	}

	public void breakBlock(BlockPos pos, ItemStack stack, World world, EntityLivingBase entityLiving, BlockPos oldPos) {
		if (oldPos == pos) {
			return;
		}

		ForgePowerItemManager capEnergy = new ForgePowerItemManager(stack);
		if (capEnergy.getEnergyStored() < cost) {
			return;
		}

		IBlockState blockState = world.getBlockState(pos);
		if (blockState.getBlockHardness(world, pos) == -1.0F) {
			return;
		}
		if(!(entityLiving instanceof EntityPlayer)){
			return;
		}

		capEnergy.extractEnergy(cost, false);
		ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityLiving);

		blockState.getBlock().harvestBlock(world, (EntityPlayer) entityLiving, pos, blockState, world.getTileEntity(pos), stack);
		world.removeBlock(pos);
		world.removeTileEntity(pos);
	}
}
