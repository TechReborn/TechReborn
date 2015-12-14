package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IAdvancedRotationTexture;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileThermalGenerator;

public class BlockThermalGenerator extends BlockMachineBase implements IAdvancedRotationTexture {



    public BlockThermalGenerator() {
        super(Material.rock);
        setUnlocalizedName("techreborn.thermalGenerator");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileThermalGenerator();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(fillBlockWithFluid(world, new BlockPos(x, y, z), player)){
            return true;
        }
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.thermalGeneratorID, world, x, y,
                    z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";


    @Override
    public String getFront(boolean isActive) {
        return isActive ? prefix + "thermal_generator_side_on" : prefix + "matter_fabricator_off";
    }

    @Override
    public String getSide(boolean isActive) {
        return isActive ? prefix + "thermal_generator_side_on" : prefix + "matter_fabricator_off";
    }

    @Override
    public String getTop(boolean isActive) {
        return isActive ? prefix + "thermal_generator_top_on" : prefix + "matter_fabricator_off";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "machine_bottom";
    }
}
