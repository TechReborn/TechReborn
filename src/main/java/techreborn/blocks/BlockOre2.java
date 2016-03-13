package techreborn.blocks;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.BaseBlock;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModBlocks;
import techreborn.world.config.IOreNameProvider;

import java.security.InvalidParameterException;
import java.util.List;

public class BlockOre2 extends BaseBlock implements ITexturedBlock, IOreNameProvider {

    public static ItemStack getOreByName(String name, int count) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(name)) {
                return new ItemStack(ModBlocks.ore2, count, i);
            }
        }
        throw new InvalidParameterException("The ore block " + name + " could not be found.");
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
        if(index == -1){
            throw new InvalidParameterException("The ore block " + name + " could not be found.");
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
        this.setDefaultState(this.getDefaultState().withProperty(METADATA, 0));
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public int damageDropped(IBlockState state) {
        int meta = getMetaFromState(state);
        return meta;
    }

    @Override
    public String getTextureNameFromState(IBlockState BlockStateContainer, EnumFacing facing) {
        return "techreborn:blocks/ore/ore" + types[getMetaFromState(BlockStateContainer)];
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

    protected BlockStateContainer createBlockStateContainer() {

        METADATA = PropertyInteger.create("Type", 0, types.length -1);
        return new BlockStateContainer(this, METADATA);
    }

    @Override
    public String getUserLoclisedName(IBlockState state) {
        return types[state.getValue(METADATA)];
    }
}
