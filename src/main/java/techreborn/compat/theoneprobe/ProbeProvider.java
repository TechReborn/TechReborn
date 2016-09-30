package techreborn.compat.theoneprobe;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.IEnergyInterfaceTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 04/06/2016.
 */
public class ProbeProvider implements IProbeInfoProvider {

	ProgressStyle euStyle = new ProgressStyle();

	public ProbeProvider() {
		euStyle.suffix(" EU");
		euStyle.numberFormat(NumberFormat.COMPACT);
	}

	@Override
	public String getID() {
		return "TechReborn";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		TileEntity tile = world.getTileEntity(data.getPos());
		if (tile instanceof IListInfoProvider) {
			List<String> strs = new ArrayList<>();
			((IListInfoProvider) tile).addInfo(strs, true);
			for (String string : strs) {
				probeInfo.text(string);
			}
		}
		if (tile instanceof IEnergyInterfaceTile) {
			IEnergyInterfaceTile energy = (IEnergyInterfaceTile) tile;
			probeInfo.progress((int) energy.getEnergy(), (int) energy.getMaxPower(), euStyle);
		}
	}
}
