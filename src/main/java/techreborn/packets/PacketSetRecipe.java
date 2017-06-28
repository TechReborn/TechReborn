package techreborn.packets;

import net.minecraft.item.crafting.IRecipe;
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
	boolean custom;

	public PacketSetRecipe(TileAutoCraftingTable tile, IRecipe recipe, boolean custom) {
		this.pos = tile.getPos();
		if (recipe == null) {
			this.recipe = new ResourceLocation("");
		} else {
			this.recipe = recipe.getRegistryName();
		}

		System.out.println(this.recipe);
		this.custom = custom;
	}

	public PacketSetRecipe() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {
		buffer.writeBlockPos(pos);
		buffer.writeResourceLocation(recipe);
		buffer.writeBoolean(custom);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {
		pos = buffer.readBlockPos();
		recipe = buffer.readResourceLocation();
		custom = buffer.readBoolean();
	}

	@Override
	public void processData(PacketSetRecipe message, MessageContext context) {
		TileEntity tileEntity = context.getServerHandler().player.world.getTileEntity(message.pos);
		;
		if (tileEntity instanceof TileAutoCraftingTable) {
			((TileAutoCraftingTable) tileEntity).setCurrentRecipe(message.recipe, message.custom);
		}
	}
}
