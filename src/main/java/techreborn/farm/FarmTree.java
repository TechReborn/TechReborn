package techreborn.farm;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import techreborn.api.farm.IFarmLogicDevice;
import techreborn.lib.Location;
import techreborn.tiles.TileFarm;
import techreborn.util.ItemUtils;

import java.util.ArrayList;

public class FarmTree implements IFarmLogicDevice {

    @Override
    public void tick(TileFarm tileFarm) {
        if(tileFarm.getWorldObj().isRemote){
            return;
        }
        if(tileFarm.getWorldObj().getTotalWorldTime() %100 == 0 || tileFarm.inventory.hasChanged){
            calculateFarmLand(tileFarm);
        }
        if(tileFarm.getWorldObj().getTotalWorldTime() %5 == 0){
            farmLandTick(tileFarm);
            saplinTick(tileFarm);
        }
    }

    ArrayList<Location> farmlandToPlace = new ArrayList<Location>();
    Block farmlandType = Blocks.dirt;
    boolean needsToPlaceWater = false;

    public void calculateFarmLand(TileFarm tileFarm){
        if(farmlandToPlace.isEmpty()){
            for (int x = -tileFarm.size + 1; x < tileFarm.size; x++) {
                for (int z = -tileFarm.size + 1; z < tileFarm.size; z++) {
                    int xpos = x + tileFarm.xCoord;
                    int ypos = tileFarm.yCoord + 1;
                    int zpos = z + tileFarm.zCoord;
                    if(xpos == tileFarm.xCoord && zpos == tileFarm.zCoord){
                        if(tileFarm.getWorldObj().getBlock(xpos,ypos, zpos) != Blocks.water){
                            needsToPlaceWater = true;
                        }
                    } else if(tileFarm.getWorldObj().getBlock(xpos,ypos, zpos) != farmlandType) {
                        farmlandToPlace.add(new Location(xpos, ypos, zpos));
                    }
                }
            }
        }
    }


    public void farmLandTick(TileFarm tileFarm){
        if(!farmlandToPlace.isEmpty()){
            Location location = farmlandToPlace.get(0);
            if(tileFarm.getWorldObj().getBlock(location.getX(),location.getY(), location.getZ()) != farmlandType){
                if(removeInputStack(new ItemStack(Blocks.dirt), tileFarm)){
                    tileFarm.getWorldObj().setBlock(location.getX(),location.getY(), location.getZ(), farmlandType);
                }
            }
            farmlandToPlace.remove(location);
        } else {
            if(needsToPlaceWater){
                //TODO use water out of tank
                tileFarm.getWorldObj().setBlock(tileFarm.xCoord, tileFarm.yCoord + 1, tileFarm.zCoord, Blocks.water);
                needsToPlaceWater = false;
            }
        }
    }

    public void saplinTick(TileFarm tileFarm){
        for (int x = -tileFarm.size + 1; x < tileFarm.size; x++) {
            for (int z = -tileFarm.size + 1; z < tileFarm.size; z++) {
                int xpos = x + tileFarm.xCoord;
                int ypos = tileFarm.yCoord + 2;
                int zpos = z + tileFarm.zCoord;
                //if(tileFarm.getWorldObj().getBlock(xpos,ypos -1, zpos) == farmlandType) {
                    if(tileFarm.getWorldObj().getBlock(xpos,ypos , zpos) == Blocks.air){
                        tileFarm.getWorldObj().setBlock(xpos,ypos , zpos, Blocks.sapling);
                    }
              //  }
            }
        }
    }

    public boolean removeInputStack(ItemStack stack, TileFarm tileFarm){
        for (int i = 1; i < 3; i++) {
            if(ItemUtils.isItemEqual(stack, tileFarm.getStackInSlot(i), true, true)){
                tileFarm.decrStackSize(i, 1);
                return true;
            }
        }
        return false;
    }


}
