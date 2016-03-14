package techreborn.blocks.storage;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.storage.TileBatBox;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class BlockBatBox  extends BlockMachineBase implements IRotationTexture {

    public BlockBatBox() {
        super();
        setUnlocalizedName("techreborn.batBox");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileBatBox();
    }

    private final String prefix = "techreborn:blocks/machine/storage/";

    @Override
    public String getFrontOff() {
        return prefix + "batbox_front";
    }

    @Override
    public String getFrontOn() {
        return prefix + "batbox_front";
    }

    @Override
    public String getSide() {
        return prefix + "batbox_side";
    }

    @Override
    public String getTop() {
        return prefix + "batbox_side";
    }

    @Override
    public String getBottom() {
        return prefix + "batbox_side";
    }

}