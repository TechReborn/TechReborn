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

import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.client.gui.GuiBase;

final class ClientTestHarness {
	static final int TEST_Y = 100;
	static final BlockPos PLAYER_POS = new BlockPos(0, TEST_Y, 0);
	static final BlockPos INTERACTION_POS = new BlockPos(2, TEST_Y, 0);
	private static final int HOTBAR_SLOT = 0;

	private final ClientGameTestContext context;
	private final TestServerContext server;

	ClientTestHarness(ClientGameTestContext context, TestServerContext server) {
		this.context = context;
		this.server = server;
	}

	void prepareWorld() {
		server.runCommand("gamemode creative @a");
		server.runCommand("time set noon");
		server.runCommand("weather clear");
		onServer(minecraftServer -> {
			ServerLevel level = level(minecraftServer);
			for (int x = -8; x <= 8; x++) {
				for (int z = -8; z <= 8; z++) {
					level.setBlockAndUpdate(new BlockPos(x, TEST_Y - 1, z), Blocks.STONE.defaultBlockState());
					level.setBlockAndUpdate(new BlockPos(x, TEST_Y, z), Blocks.AIR.defaultBlockState());
				}
			}
			minecraftServer.getPlayerList().getPlayers().getFirst().teleportTo(
				level, PLAYER_POS.getX() + 0.5, PLAYER_POS.getY(), PLAYER_POS.getZ() + 0.5,
				Set.of(), 0, 0, false
			);
		});
		context.waitTicks(2);
	}

	void placeWithInput(BlockPos pos, Block block) {
		setBlock(pos.below(), Blocks.STONE);
		setBlock(pos, Blocks.AIR);
		context.waitTick();
		context.runOnClient(client -> {
			client.player.getInventory().setSelectedSlot(HOTBAR_SLOT);
			ItemStack stack = new ItemStack(block);
			client.player.getInventory().setSelectedItem(stack);
			client.gameMode.handleCreativeModeItemAdd(stack, 36 + HOTBAR_SLOT);
		});
		useBlock(pos.below());
		context.waitTicks(3);
		assertServer(minecraftServer -> level(minecraftServer).getBlockState(pos).is(block),
			"Client placement did not place " + block.getDescriptionId());
	}

	void setBlock(BlockPos pos, Block block) {
		onServer(minecraftServer -> level(minecraftServer).setBlockAndUpdate(pos, block.defaultBlockState()));
	}

	void clearTestArea() {
		onServer(minecraftServer -> {
			ServerLevel level = level(minecraftServer);
			for (int x = -8; x <= 8; x++) {
				for (int y = TEST_Y; y <= TEST_Y + 4; y++) {
					for (int z = -8; z <= 8; z++) {
						level.setBlockAndUpdate(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());
					}
				}
			}
		});
		context.waitTicks(2);
	}

	void formMultiblock(BlockPos controllerPos, Class<? extends MachineBaseBlockEntity> expectedType) {
		onServer(minecraftServer -> {
			ServerLevel level = level(minecraftServer);
			MachineBaseBlockEntity controller = requireBlockEntity(minecraftServer, controllerPos, expectedType);
			MultiblockWriter writer = new WorldMultiblockWriter(level, controllerPos);
			controller.writeMultiblock(writer.rotate(controller.getFacing().getOpposite()));
			controller.rematch();
		});
		context.waitTicks(5);
	}

	void useBlockWithItem(BlockPos pos, ItemStack stack) {
		context.runOnClient(client -> {
			client.player.getInventory().setSelectedSlot(HOTBAR_SLOT);
			client.player.getInventory().setSelectedItem(stack);
			client.gameMode.handleCreativeModeItemAdd(stack, 36 + HOTBAR_SLOT);
		});
		useBlock(pos);
		context.waitTicks(5);
	}

	void movePlayer(double x, double y, double z) {
		onServer(minecraftServer -> minecraftServer.getPlayerList().getPlayers().getFirst().teleportTo(
			level(minecraftServer), x, y, z, Set.of(), 0, 0, false
		));
		context.waitTicks(2);
	}

	void sprintJumpForward(int ticks) {
		context.getInput().holdKey(options -> options.keyUp);
		context.getInput().holdKey(options -> options.keySprint);
		context.getInput().holdKey(options -> options.keyJump);
		context.waitTicks(ticks);
		context.getInput().releaseKey(options -> options.keyJump);
		context.getInput().releaseKey(options -> options.keySprint);
		context.getInput().releaseKey(options -> options.keyUp);
		context.waitTick();
	}

	void openUi(BlockPos pos, Class<? extends BlockEntity> expectedType, String screenshotName) {
		clearDroppedItems();
		clearHand();
		useBlock(pos);
		context.waitForScreen(GuiBase.class);
		context.runOnClient(client -> {
			Screen screen = client.gui.screen();
			if (!(screen instanceof GuiBase<?> gui) || !expectedType.isInstance(gui.be)) {
				throw new AssertionError("Unexpected UI or block entity for block at " + pos);
			}
		});
		context.waitTicks(2);
		context.takeScreenshot(screenshotName);
		context.getInput().pressKey(options -> options.keyInventory);
		context.waitForScreen(null);
	}

	void screenshot(BlockPos lookAt, String name) {
		clearDroppedItems();
		context.getInput().lookAt(lookAt);
		context.waitTicks(2);
		context.takeScreenshot(name);
	}

	void lookAt(BlockPos pos) {
		context.getInput().lookAt(pos);
		context.waitTick();
	}

	void waitTicks(int ticks) {
		context.waitTicks(ticks);
	}

	void waitForServer(Predicate<MinecraftServer> condition, int timeout, String failureMessage) {
		for (int tick = 0; tick < timeout; tick++) {
			if (server.computeOnServer(condition::test)) {
				return;
			}
			context.waitTick();
		}
		throw new AssertionError(failureMessage + " after " + timeout + " client ticks");
	}

	void assertServer(Predicate<MinecraftServer> condition, String failureMessage) {
		if (!server.computeOnServer(condition::test)) {
			throw new AssertionError(failureMessage);
		}
	}

	void onServer(Consumer<MinecraftServer> action) {
		server.runOnServer(action::accept);
	}

	static ServerLevel level(MinecraftServer server) {
		return server.overworld();
	}

	static <T> T requireBlockEntity(MinecraftServer server, BlockPos pos, Class<T> type) {
		Object blockEntity = level(server).getBlockEntity(pos);
		if (!type.isInstance(blockEntity)) {
			throw new AssertionError("Expected " + type.getSimpleName() + " at " + pos + ", got " + blockEntity);
		}
		return type.cast(blockEntity);
	}

	private void clearHand() {
		context.runOnClient(client -> {
			client.player.getInventory().setSelectedItem(ItemStack.EMPTY);
			client.gameMode.handleCreativeModeItemAdd(ItemStack.EMPTY, 36 + HOTBAR_SLOT);
		});
	}

	private void clearDroppedItems() {
		server.runCommand("kill @e[type=minecraft:item]");
	}

	private void useBlock(BlockPos lookAt) {
		context.getInput().lookAt(lookAt);
		context.waitTick();
		context.getInput().pressKey(options -> options.keyUse);
	}

	private record WorldMultiblockWriter(ServerLevel level, BlockPos origin) implements MultiblockWriter {
		@Override
		public MultiblockWriter add(int x, int y, int z, BiPredicate<BlockGetter, BlockPos> predicate, BlockState state) {
			level.setBlockAndUpdate(origin.offset(x, y, z), state);
			return this;
		}
	}
}
