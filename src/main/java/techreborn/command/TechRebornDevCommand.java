package techreborn.command;


import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fluids.Fluid;
import reborncore.api.fuel.FluidPowerManager;
import techreborn.api.recipe.RecipeHandler;
import techreborn.partSystem.IModPart;
import techreborn.partSystem.ModPartRegistry;

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
            sender.addChatMessage(new ChatComponentText("fluid     	- Lists the fluid power values"));
        } else if ("recipes".equals(args[0])) {
            sender.addChatMessage(new ChatComponentText(RecipeHandler.recipeList.size() + " recipes loaded"));
        } else if ("fluid".equals(args[0])) {
            for (Object object : FluidPowerManager.fluidPowerValues.keySet().toArray()) {
                if (object instanceof Fluid) {
                    Fluid fluid = (Fluid) object;
                    sender.addChatMessage(new ChatComponentText(fluid.getUnlocalizedName() + " : " + FluidPowerManager.fluidPowerValues.get(fluid)));
                } else {
                    sender.addChatMessage(new ChatComponentText("Found invalid fluid entry"));
                }
            }
        } else if ("parts".equals(args[0])) {
            for (IModPart part : ModPartRegistry.parts) {
                sender.addChatMessage(new ChatComponentText(part.getName()));
            }
        }
    }
}


