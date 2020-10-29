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

package techreborn.init.template;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import techreborn.init.TRContent;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TechRebornTemplates {

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, b) -> dispatcher.register(
				literal("techreborn")
						.then(literal("template")
								.requires(source -> source.hasPermissionLevel(3))
								.requires(source -> FabricLoader.getInstance().isDevelopmentEnvironment())
								.then(literal("generate")
										.then(
												argument("path", greedyString())
														.executes(TechRebornTemplates::process)
										)
								)
						)
		));
	}

	private static int process(CommandContext<ServerCommandSource> ctx) {
		Path path = Paths.get(StringArgumentType.getString(ctx, "path"));
		TemplateProcessor processor = new TemplateProcessor(path);

		try {
			process(processor);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.getSource().sendError(new LiteralText(e.getMessage()));
			return 0;
		}

		ctx.getSource().sendFeedback(new LiteralText("done"), true);

		return Command.SINGLE_SUCCESS;
	}

	private static void process(TemplateProcessor processor) throws IOException {
		processor.processSimpleBlocks("storage_blocks", Arrays.stream(TRContent.StorageBlocks.values())
				.map(storageBlocks -> storageBlocks.block)
				.collect(Collectors.toList())
		);
	}

}
