package techreborn.tiles.lesu;

import java.util.ArrayList;

public class LesuNetwork {

    public ArrayList<TileLesuStorage> storages = new ArrayList<TileLesuStorage>();

    public TileLesu master;

    public void addElement(TileLesuStorage lesuStorage){
        if(!storages.contains(lesuStorage) && storages.size() < 5000){
            storages.add(lesuStorage);
        }
    }

    public void removeElement(TileLesuStorage lesuStorage){
        storages.remove(lesuStorage);
        rebuild();
    }

    private void  rebuild(){
		master = null;
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
			if(network.master != null && this.master == null){
				this.master = network.master;
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

}
