package techreborn.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileQuantumTank;

public class BlockQuantumTank extends BlockContainer {

    public BlockQuantumTank() {
        super(Material.piston);
        setHardness(2f);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileQuantumTank();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        player.openGui(Core.INSTANCE, GuiHandler.quantumTankID, world, x, y, z);
        return true;
    }
}
