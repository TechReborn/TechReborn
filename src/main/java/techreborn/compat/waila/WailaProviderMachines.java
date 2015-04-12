package techreborn.compat.waila;

import java.util.ArrayList;
import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.tiles.TileMachineBase;

public class WailaProviderMachines implements IWailaDataProvider {

	private List<String> info = new ArrayList<String>();

	@Override
	public List<String> getWailaBody(ItemStack item, List<String> tip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

		TileMachineBase machine = (TileMachineBase) accessor.getTileEntity();

		machine.addWailaInfo(info);
		tip.addAll(info);
		info.clear();

		return tip;
	}

	@Override
	public List<String> getWailaHead(ItemStack item, List<String> tip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

		return tip;
	}

	@Override
	public List<String> getWailaTail(ItemStack item, List<String> tip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

		return tip;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {

		return null;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World w, int x, int y, int z) {

		return tag;
	}
}
