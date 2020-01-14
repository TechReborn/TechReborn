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

package techreborn.items.reactor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import ic2.api.info.Info;
import ic2.api.item.IHazmatLike;
import ic2.api.reactor.IBaseReactorComponent;
import ic2.api.reactor.IReactor;
import net.minecraftforge.fml.common.Optional;

import techreborn.items.ItemTR;

/**
 * @author estebes
 */
@Optional.Interface(iface = "ic2.api.info.Info", modid = "ic2")
@Optional.Interface(iface = "ic2.api.item.IHazmatLike", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IBaseReactorComponent", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IReactor", modid = "ic2")
public class ItemDepletedThoriumFuelRod extends ItemTR implements IBaseReactorComponent {
	public ItemDepletedThoriumFuelRod(String name, int rodCount) {
		setTranslationKey("techreborn." + name);

		this.rodCount = rodCount;
	}

	// ItemTR >>
	@Optional.Method(modid = "ic2")
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entityLiving = (EntityLivingBase) entity;

			if (!IHazmatLike.hasCompleteHazmat(entityLiving)) {
				PotionEffect effect = new PotionEffect(Info.POTION_RADIATION, 2000, 20);
				entityLiving.addPotionEffect(effect);
			}
		}
	}
	// << ItemTR

	// IBaseReactorComponent >>
	@Optional.Method(modid = "ic2")
	@Override
	public boolean canBePlacedIn(ItemStack itemStack, IReactor iReactor) {
		return false;
	}
	// << IBaseReactorComponent

	// Fields >>
	public final int rodCount;
	// << Fields
}
