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

package techreborn.items.battery;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.PoweredItemContainerProvider;
import techreborn.items.ItemTR;

import javax.annotation.Nullable;

public class ItemBattery extends ItemTR implements IEnergyItemInfo, IEnergyInterfaceItem {

	String name;
	int maxEnergy = 0;
	int maxTransfer = 0;

	public ItemBattery(String name, int maxEnergy, int maxTransfer) {
		super();
		setMaxStackSize(1);
		setMaxDamage(1);
		setUnlocalizedName("techreborn." + name);
		this.name = name;
		this.maxEnergy = maxEnergy;
		this.maxTransfer = maxTransfer;
		this.addPropertyOverride(new ResourceLocation("techreborn:empty"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack,
			                   @Nullable
				                   World worldIn,
			                   @Nullable
				                   EntityLivingBase entityIn) {
				if (stack != ItemStack.EMPTY && PoweredItem.getEnergy(stack) == 0.0) {
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}

	@Override
	public double getMaxPower(ItemStack stack) {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return maxTransfer;
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


	public double getDurabilityForDisplay(ItemStack stack) {
		double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
		return 1 - charge;
	}


	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}


	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            NBTTagCompound nbt) {
		return new PoweredItemContainerProvider(stack);
	}
}
