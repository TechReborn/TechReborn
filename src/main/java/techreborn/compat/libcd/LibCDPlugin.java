package techreborn.compat.libcd;

import io.github.cottonmc.libcd.api.LibCDInitializer;
import io.github.cottonmc.libcd.api.condition.ConditionManager;
import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import techreborn.TechReborn;
import techreborn.items.ItemDynamicCell;

public class LibCDPlugin implements LibCDInitializer {
	@Override
	public void initTweakers(TweakerManager manager) {
		manager.addTweaker("techreborn.TRTweaker", TRTweaker.INSTANCE);
		manager.addStackFactory(new Identifier(TechReborn.MOD_ID, "cell"), (id) -> ItemDynamicCell.getCellWithFluid(Registry.FLUID.get(id)));
	}

	@Override
	public void initConditions(ConditionManager manager) {

	}
}
