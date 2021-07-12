package techreborn.compat.dashloader;

import net.oskarstrom.dashloader.DashRegistry;
import net.oskarstrom.dashloader.api.annotation.DashConstructor;
import net.oskarstrom.dashloader.api.annotation.DashObject;
import net.oskarstrom.dashloader.api.enums.ConstructorMode;
import net.oskarstrom.dashloader.model.DashModel;
import techreborn.client.render.DynamicBucketBakedModel;

@DashObject(DynamicBucketBakedModel.class)
public class DashDynamicBucketBakedModel implements DashModel {

	@DashConstructor(ConstructorMode.EMPTY)
	public DashDynamicBucketBakedModel() {
	}

	@Override
	public DynamicBucketBakedModel toUndash(DashRegistry registry) {
		return new DynamicBucketBakedModel();
	}

	@Override
	public int getStage() {
		return 0;
	}
}
