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
import techreborn.tiles.generator.TileDieselGenerator;
import techreborn.tiles.generator.TileGenerator;

public class BlockGenerator extends BlockMachineBase implements IAdvancedRotationTexture {

	public BlockGenerator() {
		super(Material.iron);
		setUnlocalizedName("techreborn.generator");
	}
	
    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileGenerator();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()){
            player.openGui(Core.INSTANCE, GuiHandler.generatorID, world, x, y, z);
        }
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "machine_side";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "machine_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "diesel_generator_top_off";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "machine_bottom";
    }
}
