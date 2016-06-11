package techreborn.proxies;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import techreborn.compat.ICompatModule;

public class CommonProxy implements ICompatModule
{

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{

	}

	@Override
	public void init(FMLInitializationEvent event)
	{

	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{

	}

	public void registerFluidBlockRendering(Block block, String name){

	}

	public void registerCustomBlockStateLocation(Block block, String name) {
		registerCustomBlockStateLocation(block, name, true);
	}

	public void registerCustomBlockStateLocation(Block block, String name, boolean item) {

	}

	public void registerSubItemInventoryLocation(Item item, int meta , String location, String name){

	}

	public void registerSubBlockInventoryLocation(Block block, int meta , String location, String name){
		registerSubItemInventoryLocation(Item.getItemFromBlock(block), meta, location, name);
	}

}
