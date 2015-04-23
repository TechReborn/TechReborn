package techreborn.partSystem.parts;

import ic2.core.Ic2Items;
import ic2.core.block.wiring.TileEntityCable;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;


public class FakeCableWorld extends WorldClient {

	public int meta;

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
		TileEntityCable cable = new TileEntityCable();
		cable.setWorldObj(this);
		cable.changeFoam((byte) 1);
		cable.changeType((short) 2);
		cable.xCoord = x;
		cable.yCoord = y;
		cable.zCoord = z;
		cable.connectivity = (byte)3;
		cable.blockType =  Block.getBlockFromItem(Ic2Items.copperCableBlock.getItem());
		cable.onRender();

		return cable;
	}

	@Override
	public Chunk getChunkFromBlockCoords(int p_72938_1_, int p_72938_2_) {
		return Minecraft.getMinecraft().theWorld.getChunkFromBlockCoords(p_72938_1_, p_72938_2_);
	}

	@Override
	public boolean setBlockMetadataWithNotify(int p_72921_1_, int p_72921_2_, int p_72921_3_, int p_72921_4_, int p_72921_5_) {
		meta = p_72921_4_;
		return true;
	}

	@Override
	public int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_) {
		return meta;
	}

	@Override
	public Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_) {
		return Block.getBlockFromItem(Ic2Items.copperCableBlock.getItem());
	}
}
