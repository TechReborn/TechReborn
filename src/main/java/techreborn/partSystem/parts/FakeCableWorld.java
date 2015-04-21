package techreborn.partSystem.parts;

import ic2.core.block.wiring.TileEntityCable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkManager;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;


public class FakeCableWorld extends WorldClient {

	public FakeCableWorld() {
		super(new NetHandlerPlayClient(Minecraft.getMinecraft(), null, new NetworkManager(true)), new WorldSettings(0, WorldSettings.GameType.NOT_SET,
				false, false, WorldType.DEFAULT), 0, EnumDifficulty.PEACEFUL, Minecraft.getMinecraft().theWorld.theProfiler);
	}

	@Override
	protected IChunkProvider createChunkProvider() {
		return Minecraft.getMinecraft().thePlayer.worldObj.getChunkProvider();
	}

	@Override
	protected int func_152379_p() {
		return Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
	}

	@Override
	public Entity getEntityByID(int p_73045_1_) {
		return Minecraft.getMinecraft().theWorld.getEntityByID(p_73045_1_);
	}


	@Override
	public TileEntity getTileEntity(int x, int y, int z) {
		return new TileEntityCable();
	}

	@Override
	public Chunk getChunkFromBlockCoords(int p_72938_1_, int p_72938_2_) {
		return Minecraft.getMinecraft().theWorld.getChunkFromBlockCoords(p_72938_1_, p_72938_2_);
	}
}
