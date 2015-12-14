package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IRotationTexture;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileChemicalReactor;

public class BlockChemicalReactor extends BlockMachineBase implements IRotationTexture {



    public BlockChemicalReactor(Material material) {
        super(material);
        setUnlocalizedName("techreborn.chemicalreactor");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileChemicalReactor();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.chemicalReactorID, world, x, y,
                    z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "chemical_reactor_side_off";
    }

    @Override
    public String getFrontOn() {
        return prefix + "chemical_reactor_side_on";
    }

    @Override
    public String getSide() {
        return prefix + "machine_side";
    }

    @Override
    public String getTop() {
        return prefix + "industrial_centrifuge_top_off";
    }

    @Override
    public String getBottom() {
        return prefix + "industrial_centrifuge_bottom";
    }

}
