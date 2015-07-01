package techreborn.powerSystem;

import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import techreborn.api.power.IEnergyInterfaceTile;
import techreborn.asm.Strippable;
import techreborn.tiles.TileMachineBase;


@Optional.InterfaceList(value={
		@Optional.Interface(iface="ic2.api.energy.tile.IEnergyTile", modid="IC2", striprefs=true),
		@Optional.Interface(iface="ic2.api.energy.tile.IEnergySink", modid="IC2", striprefs=true)
})
public abstract class TilePowerAcceptor extends TileMachineBase implements
		IEnergyReceiver, //Techreborn
		IEnergyInterfaceTile,//Cofh
		IEnergyTile, IEnergySink //Ic2
{

	@Strippable("mod:ic2")
	BasicSink sink;

	@Strippable("mod:ic2")
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		sink.updateEntity();
	}

	@Strippable("mod:ic2")
	@Override
	public void invalidate()
	{
		sink.invalidate();
		super.invalidate();
	}

	@Strippable("mod:ic2")
	@Override
	public void onChunkUnload()
	{
		sink.onChunkUnload();
		super.onChunkUnload();
	}

}
