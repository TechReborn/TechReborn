package techreborn.init.template;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
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

		for (TRContent.StorageBlocks value : TRContent.StorageBlocks.values()) {
			System.out.println(Registry.BLOCK.getId(value.block));
		}

		if (true) {
			return 1;
		}

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
