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
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.ChunkStatus;
import reborncore.client.ItemStackRenderManager;
import reborncore.common.crafting.RecipeManager;

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
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RebornCoreCommands {

	private final static ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
	private final static SuggestionProvider<ServerCommandSource> MOD_SUGGESTIONS = (context, builder) ->
			CommandSource.suggestMatching(FabricLoader.getInstance().getAllMods().stream().map(modContainer -> modContainer.getMetadata().getId()), builder);

	public static void setup() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, isDedicated) -> RebornCoreCommands.addCommands(dispatcher)));
	}

	private static void addCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
				literal("reborncore")

					.then(
						literal("recipes")
							.then(literal("validate")
									.requires(source -> source.hasPermissionLevel(3))
									.executes(ctx -> {
										RecipeManager.validateRecipes(ctx.getSource().getWorld());
										return Command.SINGLE_SUCCESS;
									})
							)
					)

					.then(
						literal("generate")
							.requires(source -> source.hasPermissionLevel(3))
							.then(argument("size", integer())
									.executes(RebornCoreCommands::generate)
							)
					)

					.then(
						literal("flyspeed")
							.requires(source -> source.hasPermissionLevel(3))
							.then(argument("speed", integer(1, 10))
									.executes(ctx -> flySpeed(ctx, ImmutableList.of(ctx.getSource().getPlayer())))
									.then(CommandManager.argument("players", EntityArgumentType.players())
											.executes(ctx -> flySpeed(ctx, EntityArgumentType.getPlayers(ctx, "players")))
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
										argument("item", ItemStackArgumentType.itemStack())
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

	private static int generate(CommandContext<ServerCommandSource> ctx) {
		final int size = getInteger(ctx, "size");

		final ServerWorld world = ctx.getSource().getWorld();
		final ServerChunkManager serverChunkManager = world.getChunkManager();
		final AtomicInteger completed = new AtomicInteger(0);

		for (int x = -(size / 2); x < size / 2; x++) {
			for (int z = -(size / 2); z < size / 2; z++) {
				final int chunkPosX = x;
				final int chunkPosZ = z;
				CompletableFuture.supplyAsync(() -> serverChunkManager.getChunk(chunkPosX, chunkPosZ, ChunkStatus.FULL, true), EXECUTOR_SERVICE)
						.whenComplete((chunk, throwable) -> {
									int max = (int) Math.pow(size, 2);
									ctx.getSource().sendFeedback(new LiteralText(String.format("Finished generating %d:%d (%d/%d %d%%)", chunk.getPos().x, chunk.getPos().z, completed.getAndIncrement(), max, completed.get() == 0 ? 0 : (int) ((completed.get() * 100.0f) / max))), true);
								}
						);
			}
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int flySpeed(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> players) {
		final int speed = getInteger(ctx, "speed");
		players.stream()
				.peek(player -> player.getAbilities().setFlySpeed(speed / 20F))
				.forEach(ServerPlayerEntity::sendAbilitiesUpdate);

		return Command.SINGLE_SUCCESS;
	}

	private static int renderMod(CommandContext<ServerCommandSource> ctx) {
		String modid = StringArgumentType.getString(ctx, "modid");

		List<ItemStack> list = Registry.ITEM.getIds().stream()
				.filter(identifier -> identifier.getNamespace().equals(modid))
				.map(Registry.ITEM::get)
				.map(ItemStack::new)
				.collect(Collectors.toList());

		queueRender(list);
		return Command.SINGLE_SUCCESS;
	}

	private static int itemRenderer(CommandContext<ServerCommandSource> ctx) {
		Item item = ItemStackArgumentType.getItemStackArgument(ctx, "item").getItem();
		queueRender(Collections.singletonList(new ItemStack(item)));

		return Command.SINGLE_SUCCESS;
	}

	private static int handRenderer(CommandContext<ServerCommandSource> ctx) {
		try {
			queueRender(Collections.singletonList(ctx.getSource().getPlayer().getInventory().getMainHandStack()));
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
			return 0;
		}

		return Command.SINGLE_SUCCESS;
	}

	private static void queueRender(List<ItemStack> stacks) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			System.out.println("Render item only works on the client!");
			return;
		}
		ItemStackRenderManager.RENDER_QUEUE.addAll(stacks);
	}
}