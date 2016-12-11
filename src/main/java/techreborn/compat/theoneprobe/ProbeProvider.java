package techreborn.compat.theoneprobe;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PowerSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 04/06/2016.
 */
public class ProbeProvider implements IProbeInfoProvider {

	ProgressStyle euStyle = new ProgressStyle().backgroundColor(0xFF8B8B8B).borderColor(0xFF373737).alternateFilledColor(PowerSystem.getDisplayPower().altColour).filledColor(PowerSystem.getDisplayPower().colour);

	public ProbeProvider() {
		euStyle.suffix(" " + PowerSystem.getDisplayPower().abbreviation);
		euStyle.numberFormat(NumberFormat.COMMAS);
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
			if (PowerSystem.getDisplayPower() != PowerSystem.EnergySystem.EU) {
				probeInfo.progress((int) energy.getEnergy() * RebornCoreConfig.euPerFU, (int) energy.getMaxPower() * RebornCoreConfig.euPerFU, euStyle);
			} else {
				probeInfo.progress((int) energy.getEnergy(), (int) energy.getMaxPower(), euStyle);
			}
		}
	}
}
