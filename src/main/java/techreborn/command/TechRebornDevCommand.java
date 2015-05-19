package techreborn.command;


import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import techreborn.api.recipe.RecipeHanderer;

public class TechRebornDevCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "trdev";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "commands.forge.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.addChatMessage(new ChatComponentText("You need to use arguments"));
		} else if ("help".equals(args[0])) {
			sender.addChatMessage(new ChatComponentText("recipes - Reloads all of the machine recipes"));
		} else if ("recipes".equals(args[0])) {
			sender.addChatMessage(new ChatComponentText(RecipeHanderer.recipeList.size() + " recipes loaded"));
		}

	}
}


