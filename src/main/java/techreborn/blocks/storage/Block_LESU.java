package techreborn.blocks.storage;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.lesu.TileLesu;

public class Block_LESU extends BlockMachineBase implements IAdvancedRotationTexture {


    public Block_LESU(Material material) {
        super();
        setUnlocalizedName("techreborn.lesu");
        setCreativeTab(TechRebornCreativeTab.instance);
    }


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileLesu();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.lesuID, world, x, y,
                    z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/storage/";


    @Override
    public String getFront(boolean isActive) {
        return prefix + "lesu_front";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "lesu_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "lesu_side";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "lesu_side";
    }

}
