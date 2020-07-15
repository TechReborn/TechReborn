package techreborn.blockentity.conduit.item;

import net.minecraft.item.Item;
import reborncore.api.systems.conduit.functionalfaces.ConduitFunction;
import reborncore.api.systems.conduit.functionalfaces.IConduitItemProvider;
import techreborn.init.TRContent;

import java.util.HashMap;
import java.util.Map;

public class ConduitItems implements IConduitItemProvider {


	Map<ConduitFunction, Item> itemFunctionMap = new HashMap<ConduitFunction, Item>() {{
		put(ConduitFunction.EXPORT, TRContent.Parts.EXPORT_CONDUIT_ITEM.asItem());
		put(ConduitFunction.IMPORT, TRContent.Parts.IMPORT_CONDUIT_ITEM.asItem());
		put(ConduitFunction.BLOCK, TRContent.Parts.BLOCK_CONDUIT_ITEM.asItem());
		put(ConduitFunction.ONE_WAY, TRContent.Parts.ONE_WAY_CONDUIT_ITEM.asItem());
	}};


	@Override
	public Item getItem(ConduitFunction function) {
		return itemFunctionMap.get(function);
	}

	@Override
	public ConduitFunction fromConduitFunction(Item item) {
		for (Map.Entry<ConduitFunction, Item> entry : itemFunctionMap.entrySet()) {
			if (entry.getValue() == item) {
				return entry.getKey();
			}
		}

		return null;
	}
}
