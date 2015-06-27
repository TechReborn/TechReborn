package techreborn.compat.ee3;

import com.pahimar.ee3.command.CommandSyncEnergyValues;
import com.pahimar.ee3.exchange.DynamicEnergyValueInitThread;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.reference.Reference;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.io.File;

public class CommandRegen extends CommandBase {

	public static final String EE3_ENERGYVALUES_DIR =
			FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getSaveHandler().getWorldDirectory()
					+ File.separator +"data" + File.separator
					+ Reference.LOWERCASE_MOD_ID + File.separator
					+ "energyvalues";

	@Override
	public String getCommandName()
	{
		return "eeregen";
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
		commandSender.addChatMessage(new ChatComponentText("Regening EMC Values..."));
		File energyValuesDirectory = new File(EE3_ENERGYVALUES_DIR);

		if(energyValuesDirectory.exists() && energyValuesDirectory.isDirectory())
		{
			File [] files = energyValuesDirectory.listFiles();
			for(File f : files)
			{
				if(f.getName().toLowerCase().contains(".gz")){
					f.delete();
					commandSender.addChatMessage(new ChatComponentText("Deleted " + f.getName()));
				}

			}
		}
		commandSender.addChatMessage(new ChatComponentText("Regening EMC Values"));
		DynamicEnergyValueInitThread.initEnergyValueRegistry();
		EnergyValueRegistry.getInstance().setShouldRegenNextRestart(false);
		EnergyValueRegistry.getInstance().save();
		commandSender.addChatMessage(new ChatComponentText("Syncing all EMC Values"));
		new CommandSyncEnergyValues().processCommand(commandSender, args);
	}
}
