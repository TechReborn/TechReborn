package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import reborncore.api.tile.IContainerLayout;
import reborncore.client.gui.BaseSlot;
import reborncore.client.gui.SlotInput;
import reborncore.client.gui.SlotOutput;
import reborncore.common.recipes.RecipeCrafter;
import techreborn.api.gui.SlotUpgrade;
import techreborn.tiles.teir1.TileExtractor;

import javax.annotation.Nullable;
import java.util.List;

public class ContainerExtractor extends ContainerCrafting {

	EntityPlayer player;
	TileExtractor tileExtractor;

	public ContainerExtractor(EntityPlayer player, TileExtractor tileExtractor) {
		super(tileExtractor.crafter);
		this.player = player;
		this.tileExtractor = tileExtractor;
		// input
		this.addSlotToContainer(new SlotInput(tileExtractor.inventory, 0, 56, 34));
		this.addSlotToContainer(new SlotOutput(tileExtractor.inventory, 1, 116, 34));

		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tileExtractor.inventory, 2, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tileExtractor.inventory, 3, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tileExtractor.inventory, 4, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tileExtractor.inventory, 5, 152, 62));

		addPlayerSlots();
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}

	public void addPlayerSlots() {
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

}
