package techreborn.items.armor.modular.upgrades;

import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techreborn.api.armor.ArmorSlot;
import techreborn.api.armor.UpgradeHolder;
import techreborn.items.armor.modular.BaseArmorUprgade;
import techreborn.lib.ModInfo;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ProtectionUprgade extends BaseArmorUprgade {

	Function<DamageSource, Double> sourceIntegerFunction;
	List<ArmorSlot> validSlots = Arrays.asList(ArmorSlot.values());

	public ProtectionUprgade(String name, Function<DamageSource, Double> sourceIntegerFunction) {
		super(new ResourceLocation(ModInfo.MOD_ID, "protection_" + name));
		this.sourceIntegerFunction = sourceIntegerFunction;
	}

	public ProtectionUprgade(String name, Function<DamageSource, Double> sourceIntegerFunction, List<ArmorSlot> validSlots) {
		this(name, sourceIntegerFunction);
		this.validSlots = validSlots;
	}

	@Override
	public boolean hurt(UpgradeHolder holder, LivingHurtEvent event) {
		double reduction = sourceIntegerFunction.apply(event.getSource());
		if(reduction > 0){
			event.setAmount((float) (event.getAmount() / reduction));
		}
		return false;
	}

	@Override
	public List<ArmorSlot> getValidSlots() {
		return validSlots;
	}
}
