package techreborn.tiles.lesu;

import java.util.ArrayList;

public class LesuNetwork {

    public ArrayList<TileLesuStorage> storages = new ArrayList<TileLesuStorage>();

    public void addElement(TileLesuStorage lesuStorage){
        if(!storages.contains(lesuStorage)){
            storages.add(lesuStorage);
        }
    }

    public void removeElement(TileLesuStorage lesuStorage){
        storages.remove(lesuStorage);
        rebuild();
    }

    private void  rebuild(){
        for(TileLesuStorage lesuStorage : storages){
            lesuStorage.findAndJoinNetwork(lesuStorage.getWorldObj(), lesuStorage.xCoord, lesuStorage.yCoord, lesuStorage.zCoord);
        }
    }

    public void merge(LesuNetwork network){
        if(network != this){
            ArrayList<TileLesuStorage> tileLesuStorages = new ArrayList<TileLesuStorage>();
            tileLesuStorages.addAll(network.storages);
            network.clear(false);
            for(TileLesuStorage lesuStorage : tileLesuStorages){
                lesuStorage.setNetwork(this);
            }
        }
    }

    private void clear(boolean clearTiles) {
        if (clearTiles) {
            for(TileLesuStorage tileLesuStorage : storages){
                tileLesuStorage.resetNetwork();
            }
        }
        storages.clear();
    }


    public void printInfo(){
        System.out.println(storages.size());
    }

}
