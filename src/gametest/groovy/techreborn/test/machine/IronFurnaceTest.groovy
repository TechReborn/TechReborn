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

package techreborn.test.machine


import net.minecraft.item.Items
import net.minecraft.test.GameTest
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity
import techreborn.config.TechRebornConfig
import techreborn.init.TRContent
import techreborn.test.TRGameTest
import techreborn.test.TRTestContext

class IronFurnaceTest extends TRGameTest {
	@GameTest(templateName = "fabric-gametest-api-v1:empty", tickLimit = 2000)
	def testIronFurnaceSmeltRawIron(TRTestContext context) {
		/**
		 * Test that the Iron Furnace smelts a raw iron ore into an iron ingot in 200 ticks
		 * Verifies: Issue #2850
		 */
		context.machine(TRContent.Machine.IRON_FURNACE) {
			input(Items.COAL_BLOCK, IronFurnaceBlockEntity.FUEL_SLOT)
			input(Items.RAW_IRON, IronFurnaceBlockEntity.INPUT_SLOT)

			expectOutput(Items.IRON_INGOT, (int) (200 / TechRebornConfig.cookingScale), IronFurnaceBlockEntity.OUTPUT_SLOT)
		}
	}

	@GameTest(templateName = "fabric-gametest-api-v1:empty", tickLimit = 2000)
	def testIronFurnaceSmeltRawIronBlock(TRTestContext context) {
		/**
		 * Test that the Iron Furnace smelts a raw iron block into an iron block in 1500 ticks instead of 200
		 * Verifies: Issue #2850
		 */
		context.machine(TRContent.Machine.IRON_FURNACE) {
			input(Items.COAL_BLOCK, IronFurnaceBlockEntity.FUEL_SLOT)
			input(Items.RAW_IRON_BLOCK, IronFurnaceBlockEntity.INPUT_SLOT)

			expectOutput(Items.IRON_BLOCK, (int) (1500 / TechRebornConfig.cookingScale), IronFurnaceBlockEntity.OUTPUT_SLOT)
		}
	}
}
