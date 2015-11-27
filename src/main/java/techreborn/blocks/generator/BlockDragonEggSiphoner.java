package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IAdvancedRotationTexture;
import techreborn.tiles.TileDragonEggSiphoner;

public class BlockDragonEggSiphoner extends BlockMachineBase implements IAdvancedRotationTexture {



    public BlockDragonEggSiphoner(Material material) {
        super(material);
        setUnlocalizedName("techreborn.dragoneggsiphoner");
    }


    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileDragonEggSiphoner();
    }

    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "dragon_egg_energy_siphon_side_off";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "dragon_egg_energy_siphon_side_off" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "dragon_egg_energy_siphon_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "machine_bottom";
    }

}
