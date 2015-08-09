package techreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import techreborn.Core;
import techreborn.client.container.ContainerIDSU;
import techreborn.cofhLib.gui.GuiBase;
import techreborn.cofhLib.gui.element.ElementListBox;
import techreborn.cofhLib.gui.element.ElementTextField;
import techreborn.cofhLib.gui.element.ElementTextFieldLimited;
import techreborn.packets.PacketIdsu;
import techreborn.tiles.idsu.TileIDSU;
import techreborn.util.LogHelper;

public class GuiIDSU extends GuiBase {


    TileIDSU idsu;

    ContainerIDSU containerIDSU;

    public static ElementListBox listBox = new ElementListBox(null, 10, 28, 80, 60);

    ElementTextFieldLimited idFeild;
    ElementTextField nameFeild;


    public GuiIDSU(EntityPlayer player,
                   TileIDSU tileIDSU) {
        super(new ContainerIDSU(tileIDSU, player));
        this.xSize = 156;
        this.ySize = 200;
        idsu = tileIDSU;
        this.containerIDSU = (ContainerIDSU) this.inventorySlots;
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
        this.buttonList.add(new GuiButton(2, k + 96, l + 8 + (22 * 2), 18, 20, "-"));
        this.buttonList.add(new GuiButton(3, k + 96, l + 8 + (22 * 3), 18, 20, "--"));
        this.buttonList.add(new GuiButton(4, k + 40, l + 10, 10, 10, "+"));

        listBox.gui = this;
        listBox.setSelectedIndex(containerIDSU.channel);

//		listBox.borderColor = new GuiColor(120, 120, 120, 0).getColor();
//		listBox.backgroundColor = new GuiColor(0, 0, 0, 32).getColor();
        addElement(listBox);

        idFeild = new ElementTextFieldLimited(this, 10, 10, 30, 10, (short) 4);
        idFeild.setFilter("1234567890", false);

        addElement(idFeild);

        nameFeild = new ElementTextField(this, 10, 100, 50, 10);

        addElement(nameFeild);

//		tabs.add(new TabIDConfig(this));


    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if (isInteger(idFeild.getText())) {
            Core.packetPipeline.sendToServer(new PacketIdsu(button.id, idsu, Integer.parseInt(idFeild.getText()), nameFeild.getText()));
        } else {
            LogHelper.info("There was an issue in the gui!, Please report this to the TechReborn Devs");
        }

    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

}