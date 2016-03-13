package techreborn.blocks.generator;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileSolarPanel;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class BlockSolarPanel extends BaseTileBlock implements ITexturedBlock {

    public static PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockSolarPanel() {
        super(Material.iron);
        setUnlocalizedName("techreborn.solarpanel");
        setCreativeTab(TechRebornCreativeTab.instance);
        this.setDefaultState(this.getDefaultState().withProperty(ACTIVE, false));
    }

    protected BlockStateContainer createBlockState() {
        ACTIVE = PropertyBool.create("active");
        return new BlockStateContainer(this, ACTIVE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ACTIVE, meta == 0 ? false : true);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) == true ? 1 : 0;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSolarPanel();
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getTextureNameFromState(IBlockState state, EnumFacing side) {
        boolean isActive = state.getValue(ACTIVE);
        if(side == EnumFacing.UP){
            return prefix + "solar_panel_top_" + (isActive ? "on" : "off");
        } else if(side==EnumFacing.DOWN){
        	return prefix + "machine_bottom";
        }
        return prefix + "solar_panel_side_" + (isActive ? "on" : "off");
    }

    @Override
    public int amountOfStates() {
        return 2;
    }
}
