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

		pageCollection.addPage(new CraftingInfoPage(ModBlocks.generator.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.generator), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.thermalGenerator.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.thermalGenerator), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.solarPanel.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.solarPanel), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.heatGenerator.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.heatGenerator), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.lightningRod.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.lightningRod), "", Reference.pageNames.GENERATINGPOWER_PAGE));

		// BASIC MACHINES
		pageCollection.addPage(new BasicMachinesPage(Reference.pageNames.BASICMACHINES_PAGE, pageCollection));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.grinder.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.grinder), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.electricFurnace.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.electricFurnace), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.alloySmelter.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.alloySmelter), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.extractor.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.extractor), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.compressor.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.compressor), "", Reference.pageNames.BASICMACHINES_PAGE));

		// ADVANCED MACHINES
		pageCollection.addPage(new AdvancedMachines(Reference.pageNames.ADVANCEDMACHINES_PAGE, pageCollection));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.blastFurnace.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.blastFurnace), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.industrialSawmill.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.industrialSawmill), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.industrialElectrolyzer.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.industrialElectrolyzer), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.industrialGrinder.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.industrialGrinder), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.implosionCompressor.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.implosionCompressor), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.centrifuge.getLocalizedName(), pageCollection,
			new ItemStack(ModBlocks.centrifuge), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));

		// TOOLS
		pageCollection.addPage(new ToolsPage(Reference.pageNames.TOOLS_PAGE, pageCollection));
		pageCollection.addPage(new CraftingInfoPage(ModItems.ironDrill.getUnlocalizedName() + ".name", pageCollection,
			new ItemStack(ModItems.ironDrill), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.diamondDrill.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.diamondDrill), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.advancedDrill.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.advancedDrill), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.ironChainsaw.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.ironChainsaw), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.diamondChainsaw.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.diamondChainsaw), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.advancedChainsaw.getUnlocalizedName() + ".name",
			pageCollection, new ItemStack(ModItems.advancedChainsaw), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.omniTool.getUnlocalizedName() + ".name", pageCollection,
			new ItemStack(ModItems.omniTool), "", Reference.pageNames.TOOLS_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModItems.treeTap.getUnlocalizedName() + ".name", pageCollection,
			new ItemStack(ModItems.treeTap), "", Reference.pageNames.TOOLS_PAGE));

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
