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

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import ic2.api.info.Info;
import ic2.api.item.IHazmatLike;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import techreborn.init.ModItems;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

import static techreborn.items.reactor.ReactorUtils.ItemStackCoord;
import static techreborn.items.reactor.ReactorUtils.checkHeatAcceptor;
import static techreborn.items.reactor.ReactorUtils.checkPulseable;

/**
 * @author estebes. Credits to the IC2 team
 */
@Optional.Interface(iface = "ic2.api.info.Info", modid = "ic2")
@Optional.Interface(iface = "ic2.api.item.IHazmatLike", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IReactor", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IReactorComponent", modid = "ic2")
public class ItemThoriumFuelRod extends ItemDamageableReactorComponent {
	public ItemThoriumFuelRod(String name, int rodCount) {
		super(name, 100000);

		this.rodCount = rodCount;
	}

	// ItemTR >>
	@Optional.Method(modid = "ic2")
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entityLiving = (EntityLivingBase) entity;

			if (!IHazmatLike.hasCompleteHazmat(entityLiving)) {
				PotionEffect effect = new PotionEffect(Info.POTION_RADIATION, 200, 100);
				entityLiving.addPotionEffect(effect);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add("Time left: " + ((getMaxDamage(stack) - getDamage(stack))) + " seconds");
	}
	// << ItemTR

	// IReactorComponent >>
	@Optional.Method(modid = "ic2")
	@Override
	public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatRun) {
		if (!reactor.produceEnergy()) return;

		final int basePulses = 1 + rodCount / 2; // 1 = 1 + 0, 2 = 1 + 1, 4 = 1 + 2

		for (int iteration = 0; iteration < rodCount; iteration++) { // Perform the entire calculation for each rod-element
			int pulses = basePulses;

			if (!heatRun) {
				// Pulses itself accordingly to generate energy
				IntStream.range(0, pulses)
					.forEach(pulse -> this.acceptUraniumPulse(stack, reactor, stack, x, y, x, y, heatRun));

				checkPulseable(reactor, x - 1, y, stack, x, y, heatRun);
				checkPulseable(reactor, x + 1, y, stack, x, y, heatRun);
				checkPulseable(reactor, x, y - 1, stack, x, y, heatRun);
				checkPulseable(reactor, x, y + 1, stack, x, y, heatRun);
			} else {
				pulses += checkPulseable(reactor, x - 1, y, stack, x, y, heatRun) +
					checkPulseable(reactor, x + 1, y, stack, x, y, heatRun) +
					checkPulseable(reactor, x, y - 1, stack, x, y, heatRun) +
					checkPulseable(reactor, x, y + 1, stack, x, y, heatRun);

				int heat = (int) ((ReactorUtils.triangularNumber(pulses) * 4) * 0.2F); // More pulses (=better efficiency) => More heat

				Queue<ItemStackCoord> heatAcceptors = new ArrayDeque<>();
				checkHeatAcceptor(reactor, x - 1, y, heatAcceptors);
				checkHeatAcceptor(reactor, x + 1, y, heatAcceptors);
				checkHeatAcceptor(reactor, x, y - 1, heatAcceptors);
				checkHeatAcceptor(reactor, x, y + 1, heatAcceptors);

				while (!heatAcceptors.isEmpty() && heat > 0) { // distribute heat as evenly as possible
					int dheat = heat / heatAcceptors.size();
					heat -= dheat;
					ItemStackCoord acceptor = heatAcceptors.remove();
					IReactorComponent acceptorComp = (IReactorComponent) acceptor.stack.getItem();
					dheat = acceptorComp.alterHeat(acceptor.stack, reactor, acceptor.x, acceptor.y, dheat);
					heat += dheat; // Re-Add remaining heat
				}

				if (heat > 0) reactor.addHeat(heat);
			}
		}

		// Thorium rod is going to be depleted
		if (!heatRun && getDamage(stack) >= this.getMaxDamage(stack) - 1)
			reactor.setItemAt(x, y, getDepletedStack(stack, reactor));
		else if (!heatRun)
			setDamage(stack, getDamage(stack) + 1);
	}

	@Optional.Method(modid = "ic2")
	@Override
	public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
		if (!heatrun) reactor.addOutput(0.2F);

		return true;
	}


	@Optional.Method(modid = "ic2")
	@Override
	public float influenceExplosion(ItemStack itemStack, IReactor reactor) {
		return (2.0F * (float) rodCount) / 5.0F;
	}
	// << IReactorComponent

	// Helpers >>
	@Optional.Method(modid = "ic2")
	protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
		ItemStack ret;

		switch (rodCount) {
			case 1:
				ret = new ItemStack(ModItems.DEPLETED_THORIUM_FUEL_ROD_SINGLE);
				break;
			case 2:
				ret = new ItemStack(ModItems.DEPLETED_THORIUM_FUEL_ROD_DUAL);
				break;
			case 4:
				ret = new ItemStack(ModItems.DEPLETED_THORIUM_FUEL_ROD_QUAD);
				break;
			default: throw new RuntimeException("Invalid rod count: " + rodCount);
		}

		return ret.copy();
	}
	// << Helpers

	// Fields >>
	public final int rodCount;
	// << Fields
}
