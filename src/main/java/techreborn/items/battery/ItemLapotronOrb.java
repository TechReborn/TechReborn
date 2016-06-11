package techreborn.items.battery;

import techreborn.config.ConfigTechReborn;

public class ItemLapotronOrb extends ItemBattery {

	public ItemLapotronOrb() {
		super("lapotronicorb", ConfigTechReborn.LapotronicOrbMaxCharge, 10000, 2);
	}
}
