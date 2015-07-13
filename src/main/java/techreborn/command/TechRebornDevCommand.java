package techreborn.command;


import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import techreborn.Core;
import techreborn.api.recipe.RecipeHandler;
import techreborn.packets.PacketHandler;
import techreborn.tiles.idsu.IDSUManager;

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
			sender.addChatMessage(new ChatComponentText("You need to use arguments, see /trdev help"));
		} else if ("help".equals(args[0])) {
			sender.addChatMessage(new ChatComponentText("recipes 	- Shows size of the recipe array"));
			sender.addChatMessage(new ChatComponentText("idsu 		- Resyncs the idsu server instance to the senders client"));
		} else if ("recipes".equals(args[0])) {
			sender.addChatMessage(new ChatComponentText(RecipeHandler.recipeList.size() + " recipes loaded"));
		} else if ("idsu".equals(args[0])){
            Core.packetPipeline.sendTo(IDSUManager.INSTANCE.getPacket(sender.getEntityWorld()), (EntityPlayerMP) sender);
		}
	}
}


