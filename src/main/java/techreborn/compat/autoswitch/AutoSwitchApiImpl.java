package techreborn.compat.autoswitch;

import autoswitch.api.AutoSwitchApi;
import autoswitch.api.AutoSwitchMap;
import autoswitch.api.DurabilityGetter;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.common.util.ItemUtils;
import techreborn.items.tool.ChainsawItem;
import techreborn.items.tool.DrillItem;
import techreborn.items.tool.JackhammerItem;
import techreborn.items.tool.advanced.RockCutterItem;
import techreborn.items.tool.basic.ElectricTreetapItem;
import techreborn.items.tool.industrial.NanosaberItem;
import techreborn.items.tool.industrial.OmniToolItem;

public class AutoSwitchApiImpl implements AutoSwitchApi {

	@Override
	public void moddedTargets(AutoSwitchMap<String, Object> targets, AutoSwitchMap<String, String> actionConfig, AutoSwitchMap<String, String> usableConfig) {

	}

	@Override
	public void moddedToolGroups(AutoSwitchMap<String, Pair<Tag<Item>, Class<?>>> toolGroupings) {

	}

	@Override
	public void customDamageSystems(AutoSwitchMap<Class<?>, DurabilityGetter> damageMap) {
		// Multiple by 100 to get percentage out of decimal form
		damageMap.put(DrillItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(ChainsawItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(JackhammerItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(NanosaberItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(OmniToolItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(ElectricTreetapItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(RockCutterItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));

	}
}
