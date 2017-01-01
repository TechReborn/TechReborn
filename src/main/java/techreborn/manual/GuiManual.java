package techreborn.manual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;
import techreborn.manual.pages.*;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiManual extends GuiScreen {
	protected final PageCollection root;
	public Container inventorySlots;
	protected int pageIndex = 0;
	protected int xSize = 0;
	protected int ySize = 0;
	protected int guiLeft;
	protected int guiTop;

	public GuiManual() {
		this.xSize = 200;
		this.ySize = 180;
		root = createRoot();
		//root = ManualLoader.getPages();
	}

	protected PageCollection createRoot() {
		pageIndex = 0;
		final PageCollection pageCollection = new PageCollection();
		pageCollection.addPage(new ContentsPage(Reference.pageNames.CONTENTS_PAGE, pageCollection));

		// GETTING STARTED
		pageCollection.addPage(new GettingStartedPage(Reference.pageNames.GETTINGSTARTED_PAGE, pageCollection));

		pageCollection.addPage(new DescriptionPage(Reference.pageNames.GETTINGRUBBER_PAGE, pageCollection, true,
			Reference.pageNames.GETTINGRUBBER2_PAGE));
		pageCollection.addPage(new CraftingInfoPage(Reference.pageNames.GETTINGRUBBER2_PAGE, pageCollection,
			ItemParts.getPartByName("rubber"), "", Reference.pageNames.GETTINGRUBBER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(Reference.pageNames.CRAFTINGPLATES_PAGE, pageCollection,
			ItemPlates.getPlateByName("iron"), "", Reference.pageNames.GETTINGSTARTED_PAGE));

		// POWER GENERATION
		pageCollection.addPage(new GeneratingPowerPage(Reference.pageNames.GENERATINGPOWER_PAGE, pageCollection));

		pageCollection.addPage(new CraftingInfoPage(ModBlocks.SOLID_FUEL_GENEREATOR.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.SOLID_FUEL_GENEREATOR), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.THERMAL_GENERATOR.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.THERMAL_GENERATOR), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.SOLAR_PANEL.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.SOLAR_PANEL), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.HEAT_GENERATOR.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.HEAT_GENERATOR), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.LIGHTNING_ROD.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.LIGHTNING_ROD), "", Reference.pageNames.GENERATINGPOWER_PAGE));

		// BASIC MACHINES
		pageCollection.addPage(new BasicMachinesPage(Reference.pageNames.BASICMACHINES_PAGE, pageCollection));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.GRINDER.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.GRINDER), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.ELECTRIC_FURNACE.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.ELECTRIC_FURNACE), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.ALLOY_SMELTER.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.ALLOY_SMELTER), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.EXTRACTOR.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.EXTRACTOR), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.COMPRESSOR.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.COMPRESSOR), "", Reference.pageNames.BASICMACHINES_PAGE));

		// ADVANCED MACHINES
		pageCollection.addPage(new AdvancedMachines(Reference.pageNames.ADVANCEDMACHINES_PAGE, pageCollection));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.INDUSTRIAL_BLAST_FURNACE.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.INDUSTRIAL_BLAST_FURNACE), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.INDUSTRIAL_SAWMILL.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.INDUSTRIAL_SAWMILL), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.INDUSTRIAL_ELECTROLYZER.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.INDUSTRIAL_GRINDER.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.INDUSTRIAL_GRINDER), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.IMPLOSION_COMPRESSOR.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.IMPLOSION_COMPRESSOR), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.INDUSTRIAL_CENTRIFUGE.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));

		// TOOLS
		pageCollection.addPage(new ToolsPage(Reference.pageNames.TOOLS_PAGE, pageCollection));
		pageCollection.addPage(new CraftingInfoPage(ModItems.STEEL_DRILL.getUnlocalizedName() + ".name", pageCollection,
			new ItemStack(ModItems.STEEL_DRILL), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.DIAMOND_DRILL.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.DIAMOND_DRILL), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.ADVANCED_DRILL.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.ADVANCED_DRILL), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.STEEL_CHAINSAW.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.STEEL_CHAINSAW), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.DIAMOND_CHAINSAW.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.DIAMOND_CHAINSAW), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.ADVANCED_CHAINSAW.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.ADVANCED_CHAINSAW), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.OMNI_TOOL.getUnlocalizedName() + ".name", pageCollection,
			new ItemStack(ModItems.OMNI_TOOL), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.TREE_TAP.getUnlocalizedName() + ".name", pageCollection,
			new ItemStack(ModItems.TREE_TAP), "", Reference.pageNames.TOOLS_PAGE));

		return pageCollection;
	}

	private int getNextPageIndex() {
		int i = pageIndex;
		pageIndex++;
		return i;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		drawGuiBackgroundLayer(par3, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, par3);

		prepareRenderState();
		GL11.glPushMatrix();

		root.drawScreen(this.mc, this.guiLeft, this.guiTop, mouseX - this.guiLeft, mouseY - this.guiTop);

		GL11.glPopMatrix();
		restoreRenderState();
	}

	protected void prepareRenderState() {
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	protected void restoreRenderState() {
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}

	protected void drawGuiBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GL11.glPushMatrix();
		GL11.glTranslated(this.guiLeft, this.guiTop, 0);
		root.renderBackgroundLayer(this.mc, 0, 0, mouseX - this.guiLeft, mouseY - this.guiTop);
		GL11.glPopMatrix();
	}

	@Override
	public void setWorldAndResolution(Minecraft minecraft, int x, int y) {
		super.setWorldAndResolution(minecraft, x, y);
		root.setWorldAndResolution(minecraft, x, y);
	}

	@Override
	public void actionPerformed(GuiButton button) {
		root.actionPerformed(button);
	}

	@Override
	public void mouseClicked(int par1, int par2, int par3) throws IOException {
		root.mouseClicked(par1, par2, par3);
	}

	@Override
	public void handleInput() throws IOException {
		super.handleInput();
	}

	@Override
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
