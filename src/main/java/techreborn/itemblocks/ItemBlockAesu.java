package techreborn.itemblocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileAesu;

import java.util.List;

public class ItemBlockAesu extends ItemBlock {

    public ItemBlockAesu(Block p_i45328_1_) {
        super(p_i45328_1_);
    }

    @SuppressWarnings(
            {"rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list,
                               boolean par4) {
        if (stack != null && stack.hasTagCompound()) {
            if (stack.getTagCompound().getCompoundTag("tileEntity") != null)
                list.add(stack.getTagCompound().getCompoundTag("tileEntity")
                        .getInteger("energy")
                        + " eu");
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
                                World world, int x, int y, int z, int side, float hitX, float hitY,
                                float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, ModBlocks.Aesu, metadata, 3)) {
            return false;
        }
        if (world.getBlock(x, y, z) == ModBlocks.Aesu) {
            world.getBlock(x, y, z).onBlockPlacedBy(world, x, y, z, player,
                    stack);
            world.getBlock(x, y, z).onPostBlockPlaced(world, x, y, z, metadata);
        }
        if (stack != null && stack.hasTagCompound()) {
            ((TileAesu) world.getTileEntity(x, y, z))
                    .readFromNBTWithoutCoords(stack.getTagCompound()
                            .getCompoundTag("tileEntity"));
        }
        return true;
    }

    @SuppressWarnings(
            {"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
        itemList.add(getDropWithNBT(0));
        itemList.add(getDropWithNBT(1000000000));
    }

    public ItemStack getDropWithNBT(double energy) {
        NBTTagCompound tileEntity = new NBTTagCompound();
        ItemStack dropStack = new ItemStack(ModBlocks.Aesu, 1);
        writeToNBTWithoutCoords(tileEntity, energy);
        dropStack.setTagCompound(new NBTTagCompound());
        dropStack.stackTagCompound.setTag("tileEntity", tileEntity);
        return dropStack;
    }

    public void writeToNBTWithoutCoords(NBTTagCompound tagCompound, double energy) {
        NBTTagCompound data = new NBTTagCompound();
        data.setDouble("energy", energy);
        tagCompound.setTag("TilePowerAcceptor", data);
        tagCompound.setDouble("energy", energy);
        tagCompound.setDouble("euChange", 0);
        tagCompound.setDouble("euLastTick", 0);
        tagCompound.setBoolean("active", false);
    }
}
