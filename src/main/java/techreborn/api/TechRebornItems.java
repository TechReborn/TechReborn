package techreborn.api;

import net.minecraft.item.Item;

public class TechRebornItems
{

	public static Item getItem(String name)
	{
		try
		{
			Object e = Class.forName("techreborn.init.ModItems").getField(name).get(null);
			return e instanceof Item ? (Item) e : null;
		} catch (NoSuchFieldException e1)
		{
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 
	 * Items
	 * 
	 * gems ingots nuggets dusts smallDusts tinyDusts parts cells rockCutter
	 * lithiumBatpack lapotronpack omniTool advancedDrill lapotronicOrb manuel
	 * uuMatter plate rods crushedOre purifiedCrushedOre cloakingDevice
	 * 
	 * bucketBerylium bucketcalcium bucketcalciumcarbonate bucketChlorite
	 * bucketDeuterium bucketGlyceryl bucketHelium bucketHelium3
	 * bucketHeliumplasma bucketHydrogen bucketLithium bucketMercury
	 * bucketMethane bucketNitrocoalfuel bucketNitrofuel bucketNitrogen
	 * bucketNitrogendioxide bucketPotassium bucketSilicon bucketSodium
	 * bucketSodiumpersulfate bucketTritium bucketWolframium
	 * 
	 * hammerIron hammerDiamond upgrades farmPatten
	 * 
	 */

}
