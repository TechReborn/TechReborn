/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.clientbound.QueueItemStacksPayload;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class RebornCoreCommands {

	private final static ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
	private final static SuggestionProvider<CommandSourceStack> MOD_SUGGESTIONS = (context, builder) ->
		SharedSuggestionProvider.suggest(FabricLoader.getInstance().getAllMods().stream().map(modContainer -> modContainer.getMetadata().getId()), builder);

	public static void setup() {
		CommandRegistrationCallback.EVENT.register((RebornCoreCommands::addCommands));
	}

	private static void addCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
		dispatcher.register(
			literal("reborncore")

				.then(
					literal("generate")
						.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
						.then(argument("size", integer())
							.executes(RebornCoreCommands::generate)
						)
				)

				.then(
					literal("flyspeed")
						.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
						.then(argument("speed", integer(1, 10))
							.executes(ctx -> flySpeed(ctx, ImmutableList.of(ctx.getSource().getPlayer())))
							.then(Commands.argument("players", EntityArgument.players())
								.executes(ctx -> flySpeed(ctx, EntityArgument.getPlayers(ctx, "players")))
							)
						)
				)

				.then(
					literal("render")
						.then(
							literal("mod")
								.then(
									argument("modid", word())
										.suggests(MOD_SUGGESTIONS)
										.executes(RebornCoreCommands::renderMod)
								)
						)
						.then(
							literal("item")
								.then(
									argument("item", ItemArgument.item(registryAccess))
										.executes(RebornCoreCommands::itemRenderer)
								)
						)
						.then(
							literal("hand")
								.executes(RebornCoreCommands::handRenderer)
						)
				)
		);
	}

	private static int generate(CommandContext<CommandSourceStack> ctx) {
		final int size = getInteger(ctx, "size");

		final ServerLevel world = ctx.getSource().getLevel();
		final ServerChunkCache serverChunkManager = world.getChunkSource();
		final AtomicInteger completed = new AtomicInteger(0);

		for (int x = -(size / 2); x < size / 2; x++) {
			for (int z = -(size / 2); z < size / 2; z++) {
				final int chunkPosX = x;
				final int chunkPosZ = z;
				CompletableFuture.supplyAsync(() -> serverChunkManager.getChunk(chunkPosX, chunkPosZ, ChunkStatus.FULL, true), EXECUTOR_SERVICE)
					.whenComplete((chunk, throwable) -> {
							int max = (int) Math.pow(size, 2);
							ctx.getSource().sendSuccess(() -> Component.literal(String.format("Finished generating %d:%d (%d/%d %d%%)", chunk.getPos().x, chunk.getPos().z, completed.getAndIncrement(), max, completed.get() == 0 ? 0 : (int) ((completed.get() * 100.0f) / max))), true);
						}
					);
			}
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int flySpeed(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players) {
		final int speed = getInteger(ctx, "speed");
		players.stream()
			.peek(player -> player.getAbilities().setFlyingSpeed(speed / 20F))
			.forEach(ServerPlayer::onUpdateAbilities);

		return Command.SINGLE_SUCCESS;
	}

	private static int renderMod(CommandContext<CommandSourceStack> ctx) {
		String modid = StringArgumentType.getString(ctx, "modid");

		List<ItemStack> list = BuiltInRegistries.ITEM.keySet().stream()
			.filter(identifier -> identifier.getNamespace().equals(modid))
			.map(BuiltInRegistries.ITEM::getValue)
			.map(ItemStack::new)
			.collect(Collectors.toList());

		queueRender(list, ctx);
		return Command.SINGLE_SUCCESS;
	}

	private static int itemRenderer(CommandContext<CommandSourceStack> ctx) {
		Item item = ItemArgument.getItem(ctx, "item").getItem();
		queueRender(Collections.singletonList(new ItemStack(item)), ctx);

		return Command.SINGLE_SUCCESS;
	}

	private static int handRenderer(CommandContext<CommandSourceStack> ctx) {
		queueRender(Collections.singletonList(ctx.getSource().getPlayer().getInventory().getSelectedItem()), ctx);

		return Command.SINGLE_SUCCESS;
	}

	private static void queueRender(List<ItemStack> stacks, CommandContext<CommandSourceStack> ctx) {
		NetworkManager.sendToPlayer(new QueueItemStacksPayload(stacks), ctx.getSource().getPlayer());
	}
}
