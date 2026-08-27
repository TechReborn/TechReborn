/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.test.client;

import static techreborn.test.client.ClientTestHarness.held;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.blocks.cable.CableBlock;
import techreborn.component.TRDataComponentTypes;
import techreborn.init.ModFluids;
import techreborn.init.TRContent;

final class UtilityItemInteractionTests {
	private UtilityItemInteractionTests() {
	}

	static void run(ClientTestHarness test) {
		testPaintingToolSelectsAndAppliesCableCover(test);
		testFluidCellsPickUpPlaceAndTransform(test);
		testFluidCellsCreativeAndTechRebornFluidBehavior(test);
	}

	private static void testPaintingToolSelectsAndAppliesCableCover(ClientTestHarness test) {
		reset(test);
		BlockPos samplePos = ClientTestHarness.INTERACTION_POS;
		test.setBlock(samplePos, Blocks.STONE);
		test.setHotbarItem(new ItemStack(TRContent.PAINTING_TOOL));
		test.runCommand("gamemode survival @a");
		test.sneakUseBlockWithHeldItem(samplePos);
		test.assertServer(server -> held(server).get(TRDataComponentTypes.PAINTING_COVER) != null
			&& held(server).get(TRDataComponentTypes.PAINTING_COVER).is(Blocks.STONE),
			"Painting tool did not remember the selected solid block cover");

		BlockPos cablePos = samplePos;
		BlockPos neighborPos = cablePos.north();
		test.onServer(server -> {
			var level = ClientTestHarness.level(server);
			level.setBlockAndUpdate(cablePos, TRContent.Cables.TIN.block.defaultBlockState()
				.setValue(CableBlock.COVERED, true));
			level.setBlockAndUpdate(neighborPos, TRContent.Cables.TIN.block.defaultBlockState());
		});
		test.useBlockWithHeldItem(cablePos);
		test.assertServer(server -> {
			CableBlockEntity cable = ClientTestHarness.requireBlockEntity(server, cablePos, CableBlockEntity.class);
			return cable.getCover() != null && cable.getCover().is(Blocks.STONE)
				&& ClientTestHarness.level(server).getBlockState(cablePos).getValue(CableBlock.COVERED)
				&& ClientTestHarness.level(server).getBlockState(neighborPos).is(TRContent.Cables.TIN.block);
		}, "Painting tool did not apply the selected cover while preserving the cable network");
		test.assertServer(server -> held(server).getDamageValue() == 1,
			"Painting tool did not consume one durability when applying a cable cover");
		test.screenshot(cablePos, "items-painting-tool-stone-covered-cable");
		test.screenshotInventory("items-painting-tool-selected-cover-and-durability");
	}

	private static void testFluidCellsPickUpPlaceAndTransform(ClientTestHarness test) {
		reset(test);
		BlockPos sourcePos = ClientTestHarness.INTERACTION_POS;
		test.setBlock(sourcePos, Blocks.WATER);
		test.setHotbarItem(TRContent.Cells.EMPTY.getStack());
		test.runCommand("gamemode survival @a");
		test.screenshot(sourcePos, "items-fluid-cell-water-source-ready");
		test.useBlockWithHeldItem(sourcePos);
		test.assertServer(server -> held(server).is(TRContent.Cells.WATER.asItem())
			&& ClientTestHarness.level(server).getBlockState(sourcePos).isAir(),
			"Empty cell did not pick up and replace a water source with a water cell");

		BlockPos supportPos = sourcePos;
		test.setBlock(supportPos, Blocks.STONE);
		test.useBlockWithHeldItem(supportPos);
		BlockPos placedPos = supportPos.relative(Direction.WEST);
		test.assertServer(server -> ClientTestHarness.level(server).getFluidState(placedPos).isSource()
			&& held(server).is(TRContent.Cells.EMPTY.asItem()),
			"Water cell did not place its fluid and transform back into an empty cell");
		test.screenshot(placedPos, "items-fluid-cell-water-placed-and-emptied");

		reset(test);
		BlockPos stackedSupport = new BlockPos(2, ClientTestHarness.TEST_Y, 2);
		test.setBlock(stackedSupport, Blocks.STONE);
		test.setHotbarItem(TRContent.Cells.WATER.getStack(2));
		test.runCommand("gamemode survival @a");
		test.useBlockWithHeldItem(stackedSupport);
		test.assertServer(server -> held(server).is(TRContent.Cells.WATER.asItem()) && held(server).getCount() == 1
			&& server.getPlayerList().getPlayers().getFirst().getInventory()
			.contains(TRContent.Cells.EMPTY.getStack()),
			"Stacked water cells did not retain one filled cell and return one empty cell");
		test.screenshotInventory("items-fluid-cell-stacked-survival-replacement");
	}

	private static void testFluidCellsCreativeAndTechRebornFluidBehavior(ClientTestHarness test) {
		reset(test);
		BlockPos creativeSupport = ClientTestHarness.INTERACTION_POS;
		test.setBlock(creativeSupport, Blocks.STONE);
		test.setHotbarItem(TRContent.Cells.LAVA.getStack());
		test.useBlockWithHeldItem(creativeSupport);
		test.assertServer(server -> held(server).is(TRContent.Cells.LAVA.asItem())
			&& ClientTestHarness.level(server).getFluidState(creativeSupport.west()).isSource(),
			"Creative mode did not preserve the filled lava cell while placing fluid");
		test.screenshot(creativeSupport.west(), "items-fluid-cell-creative-lava-preserved");

		reset(test);
		BlockPos dieselSupport = ClientTestHarness.INTERACTION_POS;
		test.setBlock(dieselSupport, Blocks.STONE);
		test.setHotbarItem(TRContent.Cells.DIESEL.getStack());
		test.runCommand("gamemode survival @a");
		test.useBlockWithHeldItem(dieselSupport);
		test.assertServer(server -> ClientTestHarness.level(server).getFluidState(dieselSupport.west())
			.is(ModFluids.DIESEL.getFluid()) && held(server).is(TRContent.Cells.EMPTY.asItem()),
			"Diesel cell did not place the Tech Reborn fluid and return an empty cell");
		test.screenshot(dieselSupport.west(), "items-fluid-cell-techreborn-diesel-placed");
	}

	private static void reset(ClientTestHarness test) {
		test.resetTestState();
	}
}
