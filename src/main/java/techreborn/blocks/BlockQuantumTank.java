package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileQuantumTank;

public class BlockQuantumTank extends BlockMachineBase implements IAdvancedRotationTexture {


    public BlockQuantumTank() {
        super(Material.rock);
        setUnlocalizedName("techreborn.quantumTank");
        setHardness(2.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileQuantumTank();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(fillBlockWithFluid(world, new BlockPos(x, y, z), player)){
            return true;
        }
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.quantumTankID, world, x, y, z);
        return true;
    }

    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return  prefix + "ThermalGenerator_other";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "ThermalGenerator_other";
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "quantum_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "ThermalGenerator_other";
    }

}
