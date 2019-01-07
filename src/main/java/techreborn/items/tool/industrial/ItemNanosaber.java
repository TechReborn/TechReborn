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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.ItemUtils;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRContent;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNanosaber extends ItemSword implements IEnergyItemInfo {
	public static final int maxCharge = ConfigTechReborn.nanoSaberCharge;
	public int transferLimit = 1_000;
	public int cost = 250;

	// 4M FE max charge with 1k charge rate
	public ItemNanosaber() {
		super(ToolMaterial.DIAMOND);
		setNoRepair();
		setMaxStackSize(1);
		this.addPropertyOverride(new ResourceLocation("techreborn:active"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack,
			                   @Nullable
				                   World worldIn,
			                   @Nullable
				                   EntityLivingBase entityIn) {
				if (ItemUtils.isActive(stack)) {
					ForgePowerItemManager capEnergy = new ForgePowerItemManager(stack);
					if (capEnergy.getMaxEnergyStored() - capEnergy.getEnergyStored() >= 0.9	* capEnergy.getMaxEnergyStored()) {
						return 0.5F;
					}
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}
	
	// ItemSword
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase entityHit, EntityLivingBase entityHitter) {
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(stack);
		if (capEnergy.getEnergyStored() >= cost) {
			capEnergy.extractEnergy(cost, false);
			ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityHitter);

			return true;
		} else {
			return false;
		}
	}

	// Item
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
		int modifier = 0;
		if (ItemUtils.isActive(stack)) {
			modifier = 9;
		}
		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
				new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) modifier, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
				new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}
		return multimap;
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
					if (stack.getTagCompound() == null) {
						stack.setTagCompound(new NBTTagCompound());
					}
					stack.getTagCompound().setBoolean("isActive", true);
					if (world.isRemote) {
						ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
							TextFormatting.GRAY + I18n.format("techreborn.message.setTo") + " "
								+ TextFormatting.GOLD + I18n
								.format("techreborn.message.nanosaberActive")));
					}
				} else {
					stack.getTagCompound().setBoolean("isActive", false);
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
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (ItemUtils.isActive(stack) && new ForgePowerItemManager(stack).getEnergyStored() < cost) {
			if(worldIn.isRemote){
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
					TextFormatting.GRAY + I18n.format("techreborn.message.nanosaberEnergyError") + " "
						+ TextFormatting.GOLD + I18n
						.format("techreborn.message.nanosaberDeactivating")));
			}
			stack.getTagCompound().setBoolean("isActive", false);
		}
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            NBTTagCompound nbt) {
		return new PoweredItemContainerProvider(stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(
		CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack inactiveUncharged = new ItemStack(this);
		inactiveUncharged.setTagCompound(new NBTTagCompound());
		inactiveUncharged.getTagCompound().setBoolean("isActive", false);

		ItemStack inactiveCharged = new ItemStack(TRContent.NANOSABER);
		inactiveCharged.setTagCompound(new NBTTagCompound());
		inactiveCharged.getTagCompound().setBoolean("isActive", false);
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(inactiveCharged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		ItemStack activeCharged = new ItemStack(TRContent.NANOSABER);
		activeCharged.setTagCompound(new NBTTagCompound());
		activeCharged.getTagCompound().setBoolean("isActive", true);
		ForgePowerItemManager capEnergy2 = new ForgePowerItemManager(activeCharged);
		capEnergy2.setEnergyStored(capEnergy2.getMaxEnergyStored());

		itemList.add(inactiveUncharged);
		itemList.add(inactiveCharged);
		itemList.add(activeCharged);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack,
	                           @Nullable
		                           World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (!ItemUtils.isActive(stack)) {
			tooltip.add(TextFormatting.GRAY + I18n.format("techreborn.message.nanosaberInactive"));
		} else {
			tooltip.add(TextFormatting.GRAY + I18n.format("techreborn.message.nanosaberActive"));
		}
	}

	// IEnergyItemInfo
	@Override
	public int getCapacity() {
		return maxCharge;
	}

	@Override
	public int getMaxInput() {
		return transferLimit;
	}

	@Override
	public int getMaxOutput() {
		return 0;
	}
}
