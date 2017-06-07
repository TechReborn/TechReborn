package ic2.api.info;

import ic2.api.item.IWrenchHandler;
import ic2.api.item.IWrenchHandler.IWrenchRegistry;
import cpw.mods.fml.common.Loader;

public class IC2Classic
{
	public static IWindTicker windNetwork;
	private static IC2Type ic2 = IC2Type.NeedLoad;
	
	
	public static IC2Type getLoadedIC2Type()
	{
		if(ic2 == IC2Type.NeedLoad)
		{
			updateState();
		}
		return ic2;
	}
	
	public static boolean isIc2ExpLoaded()
	{
		if(ic2 == IC2Type.NeedLoad)
		{
			updateState();
		}
		return ic2 == IC2Type.Experimental;
	}
	
	public static boolean isIc2ClassicLoaded()
	{
		if(ic2 == IC2Type.NeedLoad)
		{
			updateState();
		}
		return ic2 == IC2Type.SpeigersClassic;
	}
	
	public static IWindTicker getWindNetwork()
	{
		return windNetwork;
	}
	
	public static boolean enabledCustoWindNetwork()
	{
		return windNetwork != null;
	}
	
	public static void registerWrenchHandler(IWrenchHandler par1)
	{
		if(isIc2ClassicLoaded())
		{
			Object obj = Info.ic2ModInstance;
			if(obj instanceof IWrenchRegistry)
			{
				((IWrenchRegistry)obj).registerWrenchSupporter(par1);
			}
		}
	}
	
	
	static void updateState()
	{
		if(Loader.isModLoaded("IC2-Classic"))
		{
			ic2 = IC2Type.ImmibisClassic;
			return;
		}
		if(Loader.isModLoaded("IC2"))
		{
			if(Loader.isModLoaded("IC2-Classic-Spmod"))
			{
				ic2 = IC2Type.SpeigersClassic;
				return;
			}
			ic2 = IC2Type.Experimental;
		}
		else
		{
			ic2 = IC2Type.None;
		}
	}
	
	public static enum IC2Type
	{
		NeedLoad,
		Experimental,
		SpeigersClassic,
		ImmibisClassic,
		None;
	}
	
}
