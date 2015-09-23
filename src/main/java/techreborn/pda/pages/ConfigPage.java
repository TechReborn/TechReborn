package techreborn.pda.pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import techreborn.config.TechRebornConfigGui;
import techreborn.init.ModItems;
import techreborn.pda.PageCollection;
import techreborn.pda.util.GuiButtonCustomTexture;

import java.io.*;
import java.lang.reflect.Type;
import java.util.TreeMap;

public class ConfigPage extends TitledPage {
	public static PDASettings pdaSettings;
    private GuiButton plusOneButton;
    private GuiButton minusOneButton;
    private GuiButton plusOneButtonScale;
    private GuiButton minusOneButtonScale;

	private static File pdaSettingsFile = new File(Minecraft.getMinecraft().mcDataDir, "TechRebornPDASettings.pda");

	public ConfigPage(String name, PageCollection collection, String unlocalizedTitle) {
		super(name, true, collection, "techreborn.pda.pdaconfig", 518915);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
        plusOneButton = new GuiButton(0, getXMin() + 85, getYMin() + 19, 10, 10, "+");
        minusOneButton = new GuiButton(1, getXMin() + 110, getYMin() + 19, 10, 10, "-");
        plusOneButtonScale = new GuiButton(2, getXMin() + 85, getYMin() + 39, 10, 10, "+");
        minusOneButtonScale = new GuiButton(3, getXMin() + 110, getYMin() + 39, 10, 10, "-");
        
        buttonList.add(plusOneButton);
        buttonList.add(minusOneButton);
        buttonList.add(plusOneButtonScale);
        buttonList.add(minusOneButtonScale);

		load();
	}
	
	@Override
	public void renderOverlayComponents(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
		this.fontRendererObj.drawString("GUI_ID", getXMin() + 20, getYMin() + 20, 518915);
		this.fontRendererObj.drawString(pdaSettings.GUI_ID + "", getXMin() + 100, getYMin() + 20, 518915);
		this.fontRendererObj.drawString("GUI_SCALE", getXMin() + 20, getYMin() + 40, 518915);
		this.fontRendererObj.drawString(pdaSettings.scale + "", getXMin() + 100, getYMin() + 40, 518915);
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 0) pdaSettings.GUI_ID++;
		if (button.id == 1 && pdaSettings.GUI_ID != 0) pdaSettings.GUI_ID--;
		if (button.id == 2) pdaSettings.scale++;
		if (button.id == 3 && pdaSettings.scale != 0) pdaSettings.scale--;

		save();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		save();
	}

	//To add new settings just make a new variable in here, the default value will be the value that you set it.

	public static class PDASettings{
		public int GUI_ID = 0;
		public float scale = 0F;
	}

	public static void load(){
		if(!pdaSettingsFile.exists()){
			pdaSettings = new PDASettings();
		} else {
			try {
				Gson gson = new Gson();
				BufferedReader reader = new BufferedReader(new FileReader(pdaSettingsFile));
				pdaSettings = gson.fromJson(reader, PDASettings.class);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				pdaSettings = new PDASettings();
			}
		}
	}
	public static void save(){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(pdaSettings);
		try {
			FileWriter writer = new FileWriter(pdaSettingsFile);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
