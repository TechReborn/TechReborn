package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.BaseBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModBlocks;

import java.security.InvalidParameterException;
import java.util.List;

public class BlockMachineFrame extends BaseBlock implements ITexturedBlock {
    public PropertyInteger METADATA;

    public static ItemStack getFrameByName(String name, int count) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equalsIgnoreCase(name)) {
                return new ItemStack(ModBlocks.machineframe, count, i);
            }
        }
        throw new InvalidParameterException("The part " + name + " could not be found.");
    }

    public static final String[] types = new String[]
            {"aluminum", "iron", "bronze", "brass", "steel", "titanium", "machine", "advancedMachine"};


    public BlockMachineFrame(Material material) {
        super(material);
        setUnlocalizedName("techreborn.machineFrame");
        setCreativeTab(TechRebornCreativeTab.instance);
        setHardness(1f);
        this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < types.length; meta++) {
            list.add(new ItemStack(item, 1, meta));
        }
    }


    @Override
    public int damageDropped(IBlockState state) {
        return super.damageDropped(state);
    }

    @Override
    public String getTextureNameFromState(IBlockState blockState, EnumFacing facing) {
        return "techreborn:blocks/machine/" + types[getMetaFromState(blockState)] + "_machine_block";
    }

    @Override
    public int amountOfStates() {
        return types.length;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(METADATA);
    }

    protected BlockState createBlockState() {

        METADATA = PropertyInteger.create("Type", 0, types.length -1);
        return new BlockState(this, METADATA);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(METADATA, meta);
    }
}
