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

package techreborn.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.util.ChatUtils;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;
import techreborn.lib.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNanosaber extends ItemSword implements IEnergyItemInfo {
	public int cost = 250;

	public ItemNanosaber() {
		super(ToolMaterial.DIAMOND);
		setNoRepair();
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setUnlocalizedName("techreborn.nanosaber");
		this.addPropertyOverride(new ResourceLocation("techreborn:active"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack,
			                   @Nullable
				                   World worldIn,
			                   @Nullable
				                   EntityLivingBase entityIn) {
				if (stack != ItemStack.EMPTY && stack.hasTagCompound() && stack.getTagCompound().hasKey("isActive") && stack.getTagCompound().getBoolean("isActive")) {
					if (PoweredItem.getMaxPower(stack) - PoweredItem.getEnergy(stack) >= 0.9 * PoweredItem.getMaxPower(stack))
						return 0.5F;
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot,
	                                                                 ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
		int modifier = 0;
		if (!stack.isEmpty() && stack.getTagCompound().getBoolean("isActive"))
			modifier = 9;

		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
				new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) modifier, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
				new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}
		return multimap;
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving,
	                         EntityLivingBase entityliving1) {
		if (PoweredItem.canUseEnergy(cost, itemstack)) {
			PoweredItem.useEnergy(cost, itemstack);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item,
	                        CreativeTabs par2CreativeTabs, NonNullList itemList) {
		ItemStack inactiveUncharged = new ItemStack(ModItems.NANOSABER);
		inactiveUncharged.setTagCompound(new NBTTagCompound());
		inactiveUncharged.getTagCompound().setBoolean("isActive", false);

		ItemStack inactiveCharged = new ItemStack(ModItems.NANOSABER);
		inactiveCharged.setTagCompound(new NBTTagCompound());
		inactiveCharged.getTagCompound().setBoolean("isActive", false);
		PoweredItem.setEnergy(getMaxPower(inactiveCharged), inactiveCharged);

		ItemStack activeCharged = new ItemStack(ModItems.NANOSABER);
		activeCharged.setTagCompound(new NBTTagCompound());
		activeCharged.getTagCompound().setBoolean("isActive", true);
		PoweredItem.setEnergy(getMaxPower(activeCharged), activeCharged);

		itemList.add(inactiveUncharged);
		itemList.add(inactiveCharged);
		itemList.add(activeCharged);
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if (stack.getTagCompound() == null || !stack.getTagCompound().getBoolean("isActive")) {
			list.add(TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.nanosaberInactive"));
		} else {
			list.add(TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.nanosaberActive"));
		}
	}


	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player,
			final EnumHand hand) {
		final ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			if (!PoweredItem.canUseEnergy(this.cost, stack)) {
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
						TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.nanosaberEnergyErrorTo") + " "
								+ TextFormatting.GOLD + I18n
								.translateToLocal("techreborn.message.nanosaberActivate")));
			} else {
				if (stack.getTagCompound() == null || !stack.getTagCompound().getBoolean("isActive")) {
					if (stack.getTagCompound() == null) {
						stack.setTagCompound(new NBTTagCompound());
					}
					stack.getTagCompound().setBoolean("isActive", true);
					if (world.isRemote && ConfigTechReborn.NanosaberChat) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
								TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.setTo") + " "
										+ TextFormatting.GOLD + I18n
										.translateToLocal("techreborn.message.nanosaberActive")));
					}
				} else {
					stack.getTagCompound().setBoolean("isActive", false);
					if (world.isRemote && ConfigTechReborn.NanosaberChat) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
								TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.setTo") + " "
										+ TextFormatting.GOLD + I18n
										.translateToLocal("techreborn.message.nanosaberInactive")));
					}
				}
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getTagCompound() != null && stack.getTagCompound().getBoolean("isActive") && !PoweredItem.canUseEnergy(cost, stack)) {
			ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
				TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.nanosaberEnergyError") + " "
					+ TextFormatting.GOLD + I18n
					.translateToLocal("techreborn.message.nanosaberDeactivating")));
			stack.getTagCompound().setBoolean("isActive", false);
		}
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public double getMaxPower(ItemStack stack) {
		return 100000;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack stack) {
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return 512;
	}

	@Override
	public int getStackTier(ItemStack stack) {
		return 2;
	}
}
