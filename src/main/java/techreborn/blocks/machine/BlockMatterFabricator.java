package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileMatterFabricator;

public class BlockMatterFabricator extends BlockMachineBase implements IAdvancedRotationTexture {

    public BlockMatterFabricator(Material material) {
        super();
        setUnlocalizedName("techreborn.matterfabricator");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileMatterFabricator();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.matterfabID, world, x, y, z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public boolean isAdvanced() {
        return true;
    }

    @Override
    public String getFront(boolean isActive) {
        return isActive ? prefix + "matter_fabricator_on" : prefix + "matter_fabricator_off";
    }

    @Override
    public String getSide(boolean isActive) {
        return isActive ? prefix + "matter_fabricator_on" : prefix + "matter_fabricator_off";
    }

    @Override
    public String getTop(boolean isActive) {
        return isActive ? prefix + "matter_fabricator_on" : prefix + "matter_fabricator_off";
    }

    @Override
    public String getBottom(boolean isActive) {
        return isActive ? prefix + "matter_fabricator_on" : prefix + "matter_fabricator_off";
    }
}
