package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.client.container.builder.BuiltContainer;

/**
 * Created by Mark on 12/04/2017.
 */
public interface IRightClickHandler {

	public boolean handleRightClick(int slotID, EntityPlayer player, BuiltContainer container);
}
