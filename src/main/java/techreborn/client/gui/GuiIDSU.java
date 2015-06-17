package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import techreborn.client.container.ContainerIDSU;
import techreborn.cofhLib.gui.GuiBase;
import techreborn.cofhLib.gui.element.ElementListBox;
import techreborn.cofhLib.gui.element.ElementTextFieldLimited;
import techreborn.cofhLib.gui.element.listbox.ListBoxElementText;
import techreborn.packets.PacketHandler;
import techreborn.packets.PacketIdsu;
import techreborn.tiles.idsu.ClientSideIDSUManager;
import techreborn.tiles.idsu.IDSUManager;
import techreborn.tiles.idsu.TileIDSU;

public class GuiIDSU extends GuiBase {


	TileIDSU idsu;

	ContainerIDSU containerIDSU;

	ElementListBox listBox;

	ElementTextFieldLimited idFeild;


	public GuiIDSU(EntityPlayer player,
				   TileIDSU tileIDSU)
	{
		super(new ContainerIDSU(tileIDSU, player));
		this.xSize = 156;
		this.ySize = 200;
		idsu = tileIDSU;
		this.containerIDSU  = (ContainerIDSU) this.inventorySlots;
		texture = new ResourceLocation(
				"techreborn", "textures/gui/aesu.png");
		drawTitle = false;
		drawInventory = false;
		name = StatCollector.translateToLocal("tile.techreborn.aesu.name");
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, k + 96, l + 8, 18, 20, "++"));
		this.buttonList.add(new GuiButton(1, k + 96, l + 8 + 22, 18, 20, "+"));
		this.buttonList.add(new GuiButton(2, k + 96, l + 8 + (22*2), 18, 20, "-"));
		this.buttonList.add(new GuiButton(3, k + 96, l + 8 + (22*3), 18, 20, "--"));
		this.buttonList.add(new GuiButton(4, k + 40, l + 10, 10, 10, "+"));

		listBox = new ElementListBox(this, 10, 28, 80, 60);

		if(idsu.getWorldObj() != null){

			for(World world : ClientSideIDSUManager.CLIENT.worldData.keySet()){
			//	System.out.println(world.n + ":" + idsu.getWorldObj().getProviderName());
				//if(world.getProviderName().equals(idsu.getWorldObj().getProviderName())){
					IDSUManager.IDSUWorldSaveData saveData = ClientSideIDSUManager.CLIENT.getWorldDataFormWorld(world);
					for(Integer id : saveData.idsuValues.keySet()){
						IDSUManager.IDSUValueSaveData valueSaveData = saveData.idsuValues.get(id);
						if(valueSaveData.name != ""){
							listBox.add(new ListBoxElementText(valueSaveData.name + " - " + id));
						} else {
							listBox.add(new ListBoxElementText(id.toString()));
						}
					}
			//	}
			}
		}
//		listBox.borderColor = new GuiColor(120, 120, 120, 0).getColor();
//		listBox.backgroundColor = new GuiColor(0, 0, 0, 32).getColor();
		addElement(listBox);

		idFeild = new ElementTextFieldLimited(this, 10, 10, 30, 10,  (short) 4);
		idFeild.setFilter("1234567890", false);

		addElement(idFeild);



	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		PacketHandler.sendPacketToServer(new PacketIdsu(button.id, idsu, Integer.parseInt(idFeild.getText()), "TestName"));
	}

}