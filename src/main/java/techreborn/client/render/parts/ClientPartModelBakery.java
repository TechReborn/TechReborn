package techreborn.client.render.parts;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.parts.powerCables.EnumCableType;

/**
 * Created by modmuss50 on 04/03/2016.
 */
public class ClientPartModelBakery {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onModelBake(ModelBakeEvent event) {
		for (EnumCableType type : EnumCableType.values()) {
			event.getModelRegistry().putObject(
				new ModelResourceLocation("techreborn:cable", "type=" + type.getName().toLowerCase()),
				new RenderCablePart(type));
		}

	}

	@SubscribeEvent
	public void textureStichEvent(TextureStitchEvent event) {
		for (EnumCableType type : EnumCableType.values()) {
			event.getMap().registerSprite(new ResourceLocation(type.textureName));
		}
	}

}
