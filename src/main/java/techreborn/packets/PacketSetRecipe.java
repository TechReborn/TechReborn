package techreborn.packets;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import techreborn.tiles.TileAutoCraftingTable;

import java.io.IOException;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class PacketSetRecipe implements INetworkPacket<PacketSetRecipe> {

	BlockPos pos;
	ResourceLocation recipe;

	public PacketSetRecipe(TileAutoCraftingTable tile, ResourceLocation recipe) {
		this.pos = tile.getPos();
		this.recipe = recipe;
		if(this.recipe == null){
			//TODO fix vanilla recipes
			this.recipe = new ResourceLocation("");
		}
	}

	public PacketSetRecipe() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {
		buffer.writeBlockPos(pos);
		buffer.writeResourceLocation(recipe);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {
		pos = buffer.readBlockPos();
		recipe = buffer.readResourceLocation();
	}

	@Override
	public void processData(PacketSetRecipe message, MessageContext context) {
		TileEntity tileEntity = context.getServerHandler().player.world.getTileEntity(pos);;
		if(tileEntity instanceof TileAutoCraftingTable){
			((TileAutoCraftingTable) tileEntity).setCurrentRecipe(recipe);
		}
	}
}
