package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IRotationTexture;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileBlastFurnace;

public class BlockBlastFurnace extends BlockMachineBase implements IRotationTexture {


    public BlockBlastFurnace(Material material) {
        super(material);
        setUnlocalizedName("techreborn.blastfurnace");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileBlastFurnace();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.blastFurnaceID, world, x, y,
                    z);
        return true;
    }



    @Override
    public boolean isAdvanced() {
        return true;
    }

    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "industrial_blast_furnace_front_off";
    }

    @Override
    public String getFrontOn() {
        return prefix + "industrial_blast_furnace_front_on";
    }

    @Override
    public String getSide() {
        return prefix + "advanced_machine_side";
    }

    @Override
    public String getTop() {
        return prefix + "advanced_machine_side";
    }

    @Override
    public String getBottom() {
        return prefix + "assembling_machine_top";
    }

}
