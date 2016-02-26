package techreborn.blocks;

import java.security.InvalidParameterException;
import java.util.List;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.BaseBlock;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModBlocks;

public class BlockOre2 extends BaseBlock implements ITexturedBlock {

    public static ItemStack getOreByName(String name, int count) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(name)) {
                return new ItemStack(ModBlocks.ore2, count, i);
            }
        }
        throw new InvalidParameterException("The storage block " + name + " could not be found.");
    }

    public static ItemStack getOreByName(String name) {
        return getOreByName(name, 1);
    }

    public IBlockState getBlockStateFromName(String name){
        int index = -1;
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(name)) {
                index = i;
                break;
            }
        }
        return getStateFromMeta(index);
    }

    public static final String[] types = new String[]
            {"copper", "tin"};

    public PropertyInteger METADATA;

    public BlockOre2(Material material) {
        super(material);
        setUnlocalizedName("techreborn.ore2");
        setCreativeTab(TechRebornCreativeTabMisc.instance);
        setHardness(2.0f);
        setHarvestLevel("pickaxe", 2);
        this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, 0));
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < types.length; meta++) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return super.getPickBlock(target, world, pos, player);
    }

    @Override
    public int damageDropped(IBlockState state) {
        int meta = getMetaFromState(state);
        return meta;
    }

    @Override
    public String getTextureNameFromState(IBlockState blockState, EnumFacing facing) {
        return "techreborn:blocks/ore/ore" + types[getMetaFromState(blockState)];
    }

    @Override
    public int amountOfStates() {
        return types.length;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(METADATA, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(METADATA);
    }

    protected BlockState createBlockState() {

        METADATA = PropertyInteger.create("Type", 0, types.length -1);
        return new BlockState(this, METADATA);
    }

}
