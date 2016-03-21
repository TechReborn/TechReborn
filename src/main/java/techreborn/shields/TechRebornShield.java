package techreborn.shields;

import net.minecraft.util.ResourceLocation;
import reborncore.shields.api.Shield;

/**
 * Created by Mark on 21/03/2016.
 */
public class TechRebornShield extends Shield {
    public TechRebornShield(String name) {
        super(name);
    }

    @Override
    public ResourceLocation getShieldTexture() {
        return new ResourceLocation("techreborn:textures/shields/trShield.png");
    }
}
