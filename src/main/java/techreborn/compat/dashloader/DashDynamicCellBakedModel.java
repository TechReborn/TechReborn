package techreborn.compat.dashloader;

import net.oskarstrom.dashloader.DashRegistry;
import net.oskarstrom.dashloader.api.annotation.DashConstructor;
import net.oskarstrom.dashloader.api.annotation.DashObject;
import net.oskarstrom.dashloader.api.enums.ConstructorMode;
import net.oskarstrom.dashloader.model.DashModel;
import techreborn.client.render.DynamicCellBakedModel;

@DashObject(DynamicCellBakedModel.class)
public class DashDynamicCellBakedModel implements DashModel {

	@DashConstructor(ConstructorMode.EMPTY)
	public DashDynamicCellBakedModel() {
	}

	@Override
	public DynamicCellBakedModel toUndash(DashRegistry registry) {
		return new DynamicCellBakedModel();
	}

	@Override
	public int getStage() {
		return 0;
	}
}
