package techreborn.powerSystem;

import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.tile.IEnergyTile;
import techreborn.api.power.IEnergyInterfaceTile;


@Optional.Interface(iface="ic2.api.energy.tile.IEnergyTile", modid="IC2", striprefs=true)
public abstract class TilePowerAcceptor implements IEnergyReceiver, IEnergyInterfaceTile, IEnergyTile {







}
