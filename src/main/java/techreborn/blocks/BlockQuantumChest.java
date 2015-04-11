package techreborn.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileQuantumChest;


public class BlockQuantumChest extends BlockContainer {

    public BlockQuantumChest() {
        super(Material.piston);
        setHardness(2f);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileQuantumChest();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        player.openGui(Core.INSTANCE, GuiHandler.quantumChestID, world, x, y, z);
        return true;
    }
}
