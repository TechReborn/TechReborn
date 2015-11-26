package techreborn.blocks;


import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TilePlayerDectector;

import java.util.List;
import java.util.Random;

public class BlockPlayerDetector extends BlockMachineBase {

    public BlockPlayerDetector() {
        super(Material.rock);
        setUnlocalizedName("techreborn.playerDetector");
        setCreativeTab(TechRebornCreativeTab.instance);
        setHardness(2f);
    }

    public static final String[] types = new String[]
            {"all", "others", "you"};


    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < types.length; meta++) {
            list.add(new ItemStack(item, 1, meta));
        }
    }



    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TilePlayerDectector();
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, BlockPos pos, IBlockState state, EnumFacing side) {
        TileEntity entity = blockAccess.getTileEntity(pos);
        if (entity instanceof TilePlayerDectector) {
            return ((TilePlayerDectector) entity).isProvidingPower() ? 15 : 0;
        }
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess blockAccess, BlockPos pos, IBlockState state, EnumFacing side) {
        TileEntity entity = blockAccess.getTileEntity(pos);
        if (entity instanceof TilePlayerDectector) {
            return ((TilePlayerDectector) entity).isProvidingPower() ? 15 : 0;
        }
        return 0;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
        super.onBlockPlacedBy(world, x, y, z, player, itemstack);
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof TilePlayerDectector) {
            ((TilePlayerDectector) tile).owenerUdid = player.getUniqueID().toString();
        }
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumFacing side, float hitX, float hitY, float hitZ) {
        // int newMeta = (world.getBlockMetadata(x, y, z) + 1) % 3;
        int newMeta = getMetaFromState(state);
        String message = "";
        switch (newMeta) {
            case 0:
                message = EnumChatFormatting.GREEN + "Detects all Players";
                break;
            case 1:
                message = EnumChatFormatting.RED + "Detects only other Players";
                break;
            case 2:
                message = EnumChatFormatting.BLUE + "Detects only you";
        }
        if(!world.isRemote){
            entityplayer.addChatComponentMessage(new ChatComponentText(message));
            //world.setBlockMetadataWithNotify(x, y, z, newMeta, 2);
        }
        return true;
    }
}
