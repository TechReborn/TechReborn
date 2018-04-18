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
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import reborncore.common.util.ChatUtils;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;
import techreborn.lib.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNanosaber extends ItemSword implements IEnergyItemInfo, IEnergyInterfaceItem {
	public int cost = 250;

	public ItemNanosaber() {
		super(ToolMaterial.DIAMOND);
		setNoRepair();
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setUnlocalizedName("techreborn.nanosaber");
		this.addPropertyOverride(new ResourceLocation("techreborn:active"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack,
			                   @Nullable
				                   World worldIn,
			                   @Nullable
				                   EntityLivingBase entityIn) {
				if (!stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey("isActive") && stack.getTagCompound().getBoolean("isActive")) {
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
		if (!stack.isEmpty() && stack.getTagCompound() != null && stack.getTagCompound().getBoolean("isActive"))
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

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(
		CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
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

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.getTagCompound() == null || !stack.getTagCompound().getBoolean("isActive")) {
			tooltip.add(TextFormatting.GRAY + I18n.format("techreborn.message.nanosaberInactive"));
		} else {
			tooltip.add(TextFormatting.GRAY + I18n.format("techreborn.message.nanosaberActive"));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player,
	                                                final EnumHand hand) {
		final ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			if (!PoweredItem.canUseEnergy(this.cost, stack)) {
				ChatUtils.sendNoSpamMessages(MessageIDs.nanosaberID, new TextComponentString(
					TextFormatting.GRAY + I18n.format("techreborn.message.nanosaberEnergyErrorTo") + " "
						+ TextFormatting.GOLD + I18n
						.format("techreborn.message.nanosaberActivate")));
			} else {
				if (stack.getTagCompound() == null || !stack.getTagCompound().getBoolean("isActive")) {
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
		if (stack.getTagCompound() != null && stack.getTagCompound().getBoolean("isActive") && !PoweredItem.canUseEnergy(cost, stack)) {
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
	public double getMaxPower(ItemStack stack) {
		return 40000;
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
	public double getEnergy(ItemStack stack) {
		NBTTagCompound tagCompound = getOrCreateNbtData(stack);
		if (tagCompound.hasKey("charge")) {
			return tagCompound.getDouble("charge");
		}
		return 0;
	}

	@Override
	public void setEnergy(double energy, ItemStack stack) {
		NBTTagCompound tagCompound = getOrCreateNbtData(stack);
		tagCompound.setDouble("charge", energy);

		if (this.getEnergy(stack) > getMaxPower(stack)) {
			this.setEnergy(getMaxPower(stack), stack);
		} else if (this.getEnergy(stack) < 0) {
			this.setEnergy(0, stack);
		}
	}

	@Override
	public double addEnergy(double energy, ItemStack stack) {
		return addEnergy(energy, false, stack);
	}

	@Override
	public double addEnergy(double energy, boolean simulate, ItemStack stack) {
		double energyReceived = Math.min(getMaxPower(stack) - energy, Math.min(this.getMaxPower(stack), energy));

		if (!simulate) {
			setEnergy(energy + energyReceived, stack);
		}
		return energyReceived;
	}


	@Override
	public boolean canUseEnergy(double input, ItemStack stack) {
		return input <= getEnergy(stack);
	}


	@Override
	public double useEnergy(double energy, ItemStack stack) {
		return useEnergy(energy, false, stack);
	}


	@Override
	public double useEnergy(double extract, boolean simulate, ItemStack stack) {
		double energyExtracted = Math.min(extract, Math.min(this.getMaxTransfer(stack), extract));

		if (!simulate) {
			setEnergy(getEnergy(stack) - energyExtracted, stack);
		}
		return energyExtracted;
	}


	@Override
	public boolean canAddEnergy(double energy, ItemStack stack) {
		return this.getEnergy(stack) + energy <= getMaxPower(stack);
	}


	public NBTTagCompound getOrCreateNbtData(ItemStack itemStack) {
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			itemStack.setTagCompound(tagCompound);
		}

		return tagCompound;
	}


	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
		return 1 - charge;
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

}
