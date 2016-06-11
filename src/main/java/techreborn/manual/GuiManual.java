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
import techreborn.manual.loader.ManualLoader;
import techreborn.manual.pages.*;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiManual extends GuiScreen
{
	protected final PageCollection root;
	public Container inventorySlots;
	protected int pageIndex = 0;
	protected int xSize = 0;
	protected int ySize = 0;
	protected int guiLeft;
	protected int guiTop;

	public GuiManual()
	{
		this.xSize = 200;
		this.ySize = 180;
		root = createRoot();
		//root = ManualLoader.getPages();
	}

	protected PageCollection createRoot()
	{
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

		pageCollection.addPage(new CraftingInfoPage(ModBlocks.Generator.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.Generator), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.thermalGenerator.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.thermalGenerator), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.solarPanel.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.solarPanel), "", Reference.pageNames.GENERATINGPOWER_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.LightningRod.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.LightningRod), "", Reference.pageNames.GENERATINGPOWER_PAGE));

		// BASIC MACHINES
		pageCollection.addPage(new BasicMachinesPage(Reference.pageNames.BASICMACHINES_PAGE, pageCollection));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.Grinder.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.Grinder), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.ElectricFurnace.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.ElectricFurnace), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.AlloySmelter.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.AlloySmelter), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.Extractor.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.Extractor), "", Reference.pageNames.BASICMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.Compressor.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.Compressor), "", Reference.pageNames.BASICMACHINES_PAGE));

		// ADVANCED MACHINES
		pageCollection.addPage(new AdvancedMachines(Reference.pageNames.ADVANCEDMACHINES_PAGE, pageCollection));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.BlastFurnace.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.BlastFurnace), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.industrialSawmill.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.industrialSawmill), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.IndustrialElectrolyzer.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.IndustrialElectrolyzer), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.IndustrialGrinder.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.IndustrialGrinder), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
		pageCollection.addPage(new CraftingInfoPage(ModBlocks.ImplosionCompressor.getLocalizedName(), pageCollection,
				new ItemStack(ModBlocks.ImplosionCompressor), "", Reference.pageNames.ADVANCEDMACHINES_PAGE));
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

	private int getNextPageIndex()
	{
		int i = pageIndex;
		pageIndex++;
		return i;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3)
	{
		drawGuiBackgroundLayer(par3, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, par3);

		prepareRenderState();
		GL11.glPushMatrix();

		root.drawScreen(this.mc, this.guiLeft, this.guiTop, mouseX - this.guiLeft, mouseY - this.guiTop);

		GL11.glPopMatrix();
		restoreRenderState();
	}

	protected void prepareRenderState()
	{
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	protected void restoreRenderState()
	{
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}

	protected void drawGuiBackgroundLayer(float p_146976_1_, int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(this.guiLeft, this.guiTop, 0);
		root.renderBackgroundLayer(this.mc, 0, 0, mouseX - this.guiLeft, mouseY - this.guiTop);
		GL11.glPopMatrix();
	}

	@Override
	public void setWorldAndResolution(Minecraft minecraft, int x, int y)
	{
		super.setWorldAndResolution(minecraft, x, y);
		root.setWorldAndResolution(minecraft, x, y);
	}

	@Override
	public void actionPerformed(GuiButton button)
	{
		root.actionPerformed(button);
	}

	@Override
	public void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		root.mouseClicked(par1, par2, par3);
	}

	@Override
	public void handleInput() throws IOException
	{
		super.handleInput();
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
