package techreborn.compat.ee3;

import com.pahimar.ee3.exchange.DynamicEnergyValueInitThread;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.reference.Files;
import com.pahimar.ee3.reference.Reference;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.io.File;

public class CommandReload extends CommandBase {

	public static final String EE3_ENERGYVALUES_DIR =
			FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getSaveHandler().getWorldDirectory()
					+ File.separator +"data" + File.separator
					+ Reference.LOWERCASE_MOD_ID + File.separator
					+ "energyvalues";

	@Override
	public String getCommandName()
	{
		return "eerelaod";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 4;
	}

	@Override
	public String getCommandUsage(ICommandSender commandSender)
	{
		return "";
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args)
	{
		commandSender.addChatMessage(new ChatComponentText("Reloading EMC Values..."));
		File energyValuesDirectory = new File(EE3_ENERGYVALUES_DIR);

		if(energyValuesDirectory.exists() && energyValuesDirectory.isDirectory()) {
			File staticValues = new File(energyValuesDirectory, Files.STATIC_ENERGY_VALUES_JSON);
			commandSender.addChatMessage(new ChatComponentText("Looking for " + staticValues.getName()));
			if(staticValues.exists()){
				commandSender.addChatMessage(new ChatComponentText("Found static values, reloading from disk!"));
			} else {
				commandSender.addChatMessage(new ChatComponentText("Will now recompute all values!!"));
			}
		}
		DynamicEnergyValueInitThread.initEnergyValueRegistry();
		EnergyValueRegistry.getInstance().setShouldRegenNextRestart(false);
	}
}