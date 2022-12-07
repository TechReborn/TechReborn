package techreborn.utils;

import techreborn.init.TRContent;

import java.util.Comparator;
import java.util.List;

public class MaterialTypeComparator implements Comparator<Enum<?>> {

	private static final List<String> ORDER = List.of(
		TRContent.Ores.class.getName(),
		TRContent.Nuggets.class.getName(),
		TRContent.SmallDusts.class.getName(),
		TRContent.Dusts.class.getName(),
		TRContent.RawMetals.class.getName(),
		TRContent.Ingots.class.getName(),
		TRContent.Gems.class.getName(),
		TRContent.Plates.class.getName(),
		TRContent.StorageBlocks.class.getName()
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
		if (o1.getClass().getName().equals(o2.getClass().getName())) {
			return 0;
		}
		return ORDER.indexOf(o1.getClass().getName()) - ORDER.indexOf(o2.getClass().getName());
	}
}
