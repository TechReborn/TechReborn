package techreborn.partSystem.parts;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import ic2.api.item.IC2Items;
import ic2.core.block.wiring.BlockCable;
import ic2.core.block.wiring.TileEntityCable;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.partSystem.fmp.FMPModPart;

import java.util.Arrays;

public class CableConverter implements MultiPartRegistry.IPartConverter {
    @Override
    public Iterable<Block> blockTypes() {
        return Arrays.asList(Block.getBlockFromItem(IC2Items.getItem("copperCableBlock").getItem()));
    }

    @Override
    public TMultiPart convert(World world, BlockCoord blockCoord) {
        Block block = world.getBlock(blockCoord.x, blockCoord.y, blockCoord.z);
        if (block instanceof BlockCable) {
            TileEntity tileEntity = world.getTileEntity(blockCoord.x, blockCoord.y, blockCoord.z);
            if (tileEntity instanceof TileEntityCable) {
                TileEntityCable cable = (TileEntityCable) tileEntity;
                int type = cable.cableType;
                CablePart newPart = new CablePart();
                newPart.setType(type);
                return new FMPModPart(newPart);
            }
        }
        return null;
    }
}
