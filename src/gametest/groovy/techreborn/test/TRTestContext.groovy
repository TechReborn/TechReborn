/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.test

import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.test.TestContext
import net.minecraft.util.math.BlockPos
import reborncore.common.blockentity.MachineBaseBlockEntity
import techreborn.init.TRContent

class TRTestContext extends TestContext {
	TRTestContext(TestContext parentContext) {
		//noinspection GroovyAccessibility
		super(parentContext.test)
	}

	/**
	 * Place a machine with a creative solar panel
	 */
	def poweredMachine(TRContent.Machine machine, @DelegatesTo(MachineContext) Closure machineContextClosure) {
		def machinePos = new BlockPos(0, 2, 0)
		setBlockState(machinePos, machine.block)
		setBlockState(machinePos.down(), TRContent.SolarPanels.CREATIVE.block)

		waitAndRun(5) {
			try {
				new MachineContext(machinePos).with(machineContextClosure)
			} catch (e) {
				e.printStackTrace()
				throw e
			}
		}
	}

	def machine(TRContent.Machine machine, @DelegatesTo(MachineContext) Closure machineContextClosure) {
		def machinePos = new BlockPos(0, 1, 0)
		setBlockState(machinePos, machine.block)

		waitAndRun(5) {
			try {
				new MachineContext(machinePos).with(machineContextClosure)
			} catch (e) {
				e.printStackTrace()
				throw e
			}
		}
	}

	class MachineContext {
		final BlockPos machinePos

		MachineContext(BlockPos machinePos) {
			this.machinePos = machinePos
		}

		def input(ItemConvertible item, int slot = -1) {
			this.input(new ItemStack(item), slot)
		}

		def input(ItemStack stack, int slot = -1) {
			if (slot == -1) {
				// If not slot is provided use the first input slot
				slot = blockEntity.crafter.inputSlots[0]
			}

			blockEntity.inventory.setStack(slot, stack)
		}

		def expectOutput(ItemConvertible item, int ticks, int slot = -1) {
			expectOutput(new ItemStack(item), ticks, slot)
		}

		def expectOutput(ItemStack stack, int ticks, int slot = -1) {
			if (slot == -1) {
				// If not slot is provided use the first output slot
				slot = blockEntity.crafter.outputSlots[0]
			}

			addFinalTaskWithDuration(ticks) {
				if (!blockEntity.inventory.getStack(slot).isItemEqual(stack)) {
					throwGameTestException("Failed to find $stack in slot $slot")
				}
			}
		}

		def withUpgrades(TRContent.Upgrades upgrade, int count = -1) {
			count = (count != -1 ? count : blockEntity.getUpgradeSlotCount()) -1

			(0..count).each {
				blockEntity.upgradeInventory.setStack(it, new ItemStack(upgrade))
			}
		}

		MachineBaseBlockEntity getBlockEntity() {
			def be = getBlockEntity(machinePos)

			if (!be) {
				throwPositionedException("Failed to get machine block entity", machinePos)
			}

			return be as MachineBaseBlockEntity
		}
	}
}
