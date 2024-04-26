package reborncore.common.entity;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RebornCoreEntityAttributes {
	public static final EntityAttribute CREATIVE_FLIGHT = new ClampedEntityAttribute("reborncore:creative_flight", 0.0D, 0, Double.POSITIVE_INFINITY).setTracked(true);

	public static void init() {
		Registry.register(Registries.ATTRIBUTE, new Identifier("reborncore:creative_flight"), CREATIVE_FLIGHT);
	}
}
