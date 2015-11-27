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
import techreborn.tiles.TileSemifluidGenerator;

public class BlockSemiFluidGenerator extends BlockMachineBase implements IAdvancedRotationTexture {



    public BlockSemiFluidGenerator(Material material) {
        super(material);
        setUnlocalizedName("techreborn.semifluidgenerator");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileSemifluidGenerator();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(fillBlockWithFluid(world, new BlockPos(x, y, z), player)){
            return true;
        }
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.semifluidGeneratorID, world, x, y,
                    z);
        return true;
    }

    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "semifluid_generator_side";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "semifluid_generator_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "machine_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "machine_bottom";
    }

}
