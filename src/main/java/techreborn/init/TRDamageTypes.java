package techreborn.init;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import techreborn.TechReborn;

public final class TRDamageTypes {
	public static final RegistryKey<DamageType> ELECTRIC_SHOCK = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(TechReborn.MOD_ID, "electric_shock"));
	public static final RegistryKey<DamageType> FUSION = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(TechReborn.MOD_ID, "fusion"));

	public static DamageSource create(World world, RegistryKey<DamageType> key) {
		return new DamageSource(
			world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key)
		);
	}
}
