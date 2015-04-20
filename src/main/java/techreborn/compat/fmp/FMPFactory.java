package techreborn.compat.fmp;

import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import techreborn.client.TechRebornCreativeTab;

public class FMPFactory implements MultiPartRegistry.IPartFactory {

	static Item cableItem;

	@Override
	public TMultiPart createPart(String s, boolean b) {
		if(s.equals("TRCable")){
			return new CablePart();
		}
		return null;
	}

	public static void init(){
		MultiPartRegistry.registerParts(new FMPFactory(), new String[]{"TRCable"});
		cableItem = new ItemCable().setCreativeTab(TechRebornCreativeTab.instance).setUnlocalizedName("TRCable");
		GameRegistry.registerItem(cableItem, "TRCable");
	}

}
