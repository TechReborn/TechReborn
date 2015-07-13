package techreborn.farm;

import techreborn.api.farm.IFarmLogicDevice;
import techreborn.tiles.TileFarm;

public class FarmTree implements IFarmLogicDevice {



    @Override
    public void tick(TileFarm tileFarm) {
        System.out.println("tick");
    }
}
