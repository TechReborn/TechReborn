package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IRotationTexture;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileIndustrialElectrolyzer;

public class BlockIndustrialElectrolyzer extends BlockMachineBase implements IRotationTexture {


    public BlockIndustrialElectrolyzer(Material material) {
        super(material);
        setUnlocalizedName("techreborn.industrialelectrolyzer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileIndustrialElectrolyzer();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.industrialElectrolyzerID, world, x, y,
                    z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "industrial_electrolyzer_front_off";
    }

    @Override
    public String getFrontOn() {
        return prefix + "industrial_electrolyzer_front_on";
    }

    @Override
    public String getSide() {
        return prefix + "industrial_electrolyzer_front_off";
    }

    @Override
    public String getTop() {
        return prefix + "machine_top";
    }

    @Override
    public String getBottom() {
        return prefix + "machine_bottom";
    }

}
