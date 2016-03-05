package techreborn.client.render.parts;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.parts.CableMultipart;
import techreborn.parts.EnumCableType;

/**
 * Created by Mark on 04/03/2016.
 */
public class ClientPartModelBakery {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onModelBake(ModelBakeEvent event){
        for(EnumCableType type : EnumCableType.values()){
            event.modelRegistry.putObject(new ModelResourceLocation("techreborn:cable#type=" + type.getName().toLowerCase()), new RenderCablePart(type));
        }

    }

}
