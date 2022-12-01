package techreborn.utils;

import java.util.Comparator;
import java.util.List;

public class MaterialComparator implements Comparator<Enum<?>> {

	private static final List<String> ORDER = List.of(
		"SAW",
		"WOOD",
		"ASHES",
		"DARK_ASHES",
		"ANDESITE",
		"DIORITE",
		"GRANITE",
		"BASALT",
		"CALCITE",
		"MARBLE",
		"COAL",
		"CARBON",
		"CARBON_FIBER",
		"CHARCOAL",
		"CLAY",
		"FLINT",
		"SILICON",
		"TIN",
		"ZINC",
		"IRON",
		"REFINED_IRON",
		"STEEL",
		"COPPER",
		"LEAD",
		"NICKEL",
		"BRONZE",
		"BRASS",
		"MIXED_METAL",
		"ADVANCED_ALLOY",
		"INVAR",
		"SILVER",
		"GOLD",
		"ELECTRUM",
		"GALENA",
		"BAUXITE",
		"ALUMINUM",
		"TITANIUM",
		"CHROME",
		"REDSTONE",
		"RUBY",
		"SAPPHIRE",
		"PERIDOT",
		"RED_GARNET",
		"YELLOW_GARNET",
		"EMERALD",
		"AMETHYST",
		"LAPIS",
		"LAZURITE",
		"DIAMOND",
		"IRIDIUM",
		"IRIDIUM_ALLOY",
		"PLATINUM",
		"OBSIDIAN",
		"NETHERRACK",
		"GLOWSTONE",
		"QUARTZ",
		"CINNABAR",
		"PYRITE",
		"PYROPE",
		"SPHALERITE",
		"ALMANDINE",
		"ANDRADITE",
		"GROSSULAR",
		"SPESSARTINE",
		"MAGNESIUM",
		"MANGANESE",
		"MAGNALIUM",
		"PHOSPHOROUS",
		"SULFUR",
		"SALTPETER",
		"OLIVINE",
		"UVAROVITE",
		"ENDER_PEARL",
		"ENDER_EYE",
		"ENDSTONE",
		"SODALITE",
		"SHELDONITE",
		"TUNGSTEN",
		"TUNGSTENSTEEL",
		"HOT_TUNGSTENSTEEL",
		"IRIDIUM_REINFORCED_STONE",
		"IRIDIUM_REINFORCED_TUNGSTENSTEEL",
		"NETHERITE"
	);

	@Override
	public int compare(Enum<?> o1, Enum<?> o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		if (o1.name().equals(o2.name())) {
			return 0;
		}
		return ORDER.indexOf(o1.name()) - ORDER.indexOf(o2.name());
	}
}
