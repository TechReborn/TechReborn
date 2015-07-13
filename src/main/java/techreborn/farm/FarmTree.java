package techreborn.farm;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import techreborn.api.farm.IFarmLogicDevice;
import techreborn.lib.Location;
import techreborn.tiles.TileFarm;
import techreborn.util.ItemUtils;

import java.util.ArrayList;

public class FarmTree implements IFarmLogicDevice {

    public static ArrayList<Block> harvestableLogs = new ArrayList<Block>();
    ArrayList<Location> farmlandToPlace = new ArrayList<Location>();
    Block farmlandType = Blocks.dirt;
    int harvestx = 0;
    int harvesty = 0;
    int harvestz = 0;
    boolean isHavrvesting = false;

    @Override
    public void tick(TileFarm tileFarm) {
        if (tileFarm.getWorldObj().isRemote) {
            return;
        }
        if (tileFarm.getWorldObj().getTotalWorldTime() % 20 == 0 || tileFarm.inventory.hasChanged) {
            calculateFarmLand(tileFarm);
        }
        if (tileFarm.getWorldObj().getTotalWorldTime() % 10 == 0) {
            farmLandTick(tileFarm);
            saplinTick(tileFarm);
        }
        for (int i = 0; i < 5; i++) {
            harvestTick(tileFarm);
        }
    }

    public void calculateFarmLand(TileFarm tileFarm) {
        if (farmlandToPlace.isEmpty()) {
            for (int x = -tileFarm.size + 1; x < tileFarm.size; x++) {
                for (int z = -tileFarm.size + 1; z < tileFarm.size; z++) {
                    int xpos = x + tileFarm.xCoord;
                    int ypos = tileFarm.yCoord + 1;
                    int zpos = z + tileFarm.zCoord;
                    if (tileFarm.getWorldObj().getBlock(xpos, ypos, zpos) != farmlandType) {
                        farmlandToPlace.add(new Location(xpos, ypos, zpos));
                    }
                }
            }
        }
    }

    public void farmLandTick(TileFarm tileFarm) {
        if (!farmlandToPlace.isEmpty()) {
            Location location = farmlandToPlace.get(0);
            if (tileFarm.getWorldObj().getBlock(location.getX(), location.getY(), location.getZ()) != farmlandType) {
                if (removeInputStack(new ItemStack(Blocks.dirt), tileFarm)) {
                    tileFarm.getWorldObj().setBlock(location.getX(), location.getY(), location.getZ(), farmlandType);
                }
            }
            farmlandToPlace.remove(location);
        }
    }

    int sapx = 0;
    int sapz = 0;
    boolean isplanting;

    //TODO use saplins from inv
    public void saplinTick(TileFarm tileFarm) {
        if(!isplanting){
            sapx = - tileFarm.size;
            sapz = -tileFarm.size;
            isplanting = true;
        } else {
            int xpos = sapx + tileFarm.xCoord;
            int ypos = tileFarm.yCoord + 2;
            int zpos = sapz + tileFarm.zCoord;
            if(getSaplinStack(tileFarm) != null){
                Block saplin = Block.getBlockFromItem(getSaplinStack(tileFarm).getItem());
                int meta = getSaplinStack(tileFarm).getItemDamage();
                if (saplin != null && tileFarm.getWorldObj().getBlock(xpos, ypos, zpos) == Blocks.air && saplin.canBlockStay(tileFarm.getWorldObj(), xpos, ypos, zpos) && saplin.canPlaceBlockAt(tileFarm.getWorldObj(), xpos, ypos, zpos)) {
                    tileFarm.getWorldObj().setBlock(xpos, ypos, zpos, saplin, meta, 2);
                }
                sapx ++;
                if(sapx == tileFarm.size){
                    sapx =  - tileFarm.size;
                    sapz ++;
                }
                if(sapz >= tileFarm.size){
                    sapz = -tileFarm.size;
                    sapx = -tileFarm.size;
                }
            }
        }
    }

    public void harvestTick(TileFarm tileFarm) {
        int overlap = 2;
        if (farmlandToPlace.isEmpty()) {
            if (!isHavrvesting) {
                harvestx = -tileFarm.size - overlap;
                harvesty = + 2;
                harvestz = -tileFarm.size - overlap;
                isHavrvesting = true;
            } else {
                Block block = tileFarm.getWorldObj().getBlock(harvestx + tileFarm.xCoord, harvesty + tileFarm.yCoord, harvestz + tileFarm.zCoord);
                if(block instanceof BlockLeavesBase){
                    tileFarm.getWorldObj().setBlockToAir(harvestx + tileFarm.xCoord, harvesty + tileFarm.yCoord, harvestz + tileFarm.zCoord);
                } else if(harvestableLogs.contains(block)){
                    tileFarm.getWorldObj().setBlockToAir(harvestx + tileFarm.xCoord, harvesty + tileFarm.yCoord, harvestz + tileFarm.zCoord);
                }
                    harvestx ++;
                 if(harvestx >= tileFarm.size + overlap){
                    harvestx = -tileFarm.size - overlap;
                    harvestz ++;
                } else if(harvestz >= tileFarm.size + overlap){
                    harvestx = -tileFarm.size - overlap;
                    harvestz = -tileFarm.size - overlap;
                    harvesty ++;
                } else if(harvesty > 12){
                    harvestx = 0;
                    harvesty = 2;
                    harvestz = 0;
                    isHavrvesting = false;
                }
            }
        }
    }

    public boolean removeInputStack(ItemStack stack, TileFarm tileFarm) {
        for (int i = 1; i < 3; i++) {
            if (ItemUtils.isItemEqual(stack, tileFarm.getStackInSlot(i), true, true)) {
                tileFarm.decrStackSize(i, 1);
                return true;
            }
        }
        return false;
    }


    public ItemStack getSaplinStack(TileFarm tileFarm) {
        for (int i = 1; i < 14; i++) {
            if(tileFarm.getStackInSlot(i) != null && Block.getBlockFromItem(tileFarm.getStackInSlot(i).getItem()) instanceof BlockSapling){
                return tileFarm.getStackInSlot(i);
            }
        }
        return null;
    }

}
