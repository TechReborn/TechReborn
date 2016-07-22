package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import reborncore.client.gui.BaseSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.client.gui.SlotOutput;
import reborncore.common.container.RebornContainer;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.tiles.fusionReactor.TileEntityFusionController;

public class ContainerFusionReactor extends RebornContainer {

	public int tickTime;
	public int finalTickTime;

	public int neededEnergy;
	public int energy;

	public boolean hasCoils;

	TileEntityFusionController fusionController;

	public ContainerFusionReactor(TileEntityFusionController tileEntityFusionController, EntityPlayer player) {
		super();
		this.fusionController = tileEntityFusionController;

		addSlotToContainer(new BaseSlot(tileEntityFusionController, 0, 88, 17));
		addSlotToContainer(new BaseSlot(tileEntityFusionController, 1, 88, 53));
		addSlotToContainer(new SlotOutput(tileEntityFusionController, 2, 148, 35));

		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		FusionReactorRecipe rec = fusionController.recipe;
		for (int i = 0; i < this.listeners.size(); i++) {
			IContainerListener listener = this.listeners.get(i);
			listener.sendProgressBarUpdate(this, 0, fusionController.hasCoils ? 1 : 0);
			listener.sendProgressBarUpdate(this, 1, (int) fusionController.getEnergy());
			listener.sendProgressBarUpdate(this, 2, rec == null ? 0 : (int) rec.getStartEU());
			listener.sendProgressBarUpdate(this, 3, rec == null ? -1 : fusionController.recipeTickTime);
			listener.sendProgressBarUpdate(this, 4, rec == null ? 0 : rec.getTickTime());
		}
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		FusionReactorRecipe rec = fusionController.recipe;
		listener.sendProgressBarUpdate(this, 0, fusionController.hasCoils ? 1 : 0);
		listener.sendProgressBarUpdate(this, 1, (int) fusionController.getEnergy());
		listener.sendProgressBarUpdate(this, 2, rec == null ? 0 : (int) rec.getStartEU());
		listener.sendProgressBarUpdate(this, 3, rec == null ? -1 : fusionController.recipeTickTime);
		listener.sendProgressBarUpdate(this, 4, rec == null ? 0 : rec.getTickTime());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		switch (id) {
			case 0:
				this.hasCoils = value == 1;
				break;
			case 1:
				this.energy = value;
				break;
			case 2:
				this.neededEnergy = value;
				break;
			case 3:
				this.tickTime = value;
				break;
			case 4:
				this.finalTickTime = value;
				break;
		}
	}

	public int getProgressScaled() {
		return Math.max(0, Math.min(24,
				(this.tickTime > 0 ? 1 : 0) + this.tickTime * 24 / (this.finalTickTime < 1 ? 1 : this.finalTickTime)));
	}

}
