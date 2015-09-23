package techreborn.pda;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.pda.pages.ConfigPage;
import techreborn.pda.pages.ContentsPage;
import techreborn.pda.pages.CraftingInfoPage;
import techreborn.pda.pages.IndexPage;
import techreborn.pda.pages.ItemsPage;
import techreborn.pda.pages.MultiBlockPage;
import techreborn.pda.pages.TitledPage;
import techreborn.pda.pages.VersionPage;

@SideOnly(Side.CLIENT)
public class GuiManual extends GuiScreen{
	
	protected final PageCollection root;
	protected int pageIndex = 0;
	protected int xSize = 0;
	protected int ySize = 0;
	public Container inventorySlots;
	protected int guiLeft;
	protected int guiTop;

	public GuiManual() {
		this.xSize = 256;
		this.ySize = 202;
		root = createRoot();
	}

	protected PageCollection createRoot() {
		pageIndex = 0;
		final PageCollection pageCollection = new PageCollection();
		pageCollection.addPage(new IndexPage("INDEX", pageCollection));
		pageCollection.addPage(new ContentsPage("CONTENTS", pageCollection));
		pageCollection.addPage(new ItemsPage("ITEMS", pageCollection, "ITEM_PAGE"));
		pageCollection.addPage(new ItemsPage("TOOLS", pageCollection, "TOOLS_PAGE"));
		pageCollection.addPage(new ItemsPage("UPGRADES", pageCollection, "UPGRADES_PAGE"));

		pageCollection.addPage(new ItemsPage("MACHINES", pageCollection, "MACHINES_PAGE"));
		pageCollection.addPage(new ItemsPage("POWER_GENERATION", pageCollection, "POWER_GENERATION_PAGE"));
		pageCollection.addPage(new ItemsPage("POWER_STORAGE", pageCollection, "POWER_STORAGE_PAGE"));
		pageCollection.addPage(new VersionPage("VERSION", pageCollection, "VERSION PAGE", 777777));
		pageCollection.addPage(new MultiBlockPage("MULTIBLOCKS", pageCollection, "MULTIBLOCK_PAGE"));
		pageCollection.addPage(new ConfigPage("CONFIG", pageCollection, "PDA_CONFIG"));

		pageCollection.addPage(new CraftingInfoPage("POWER_STORAGE_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Aesu), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.AlloyFurnace), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.AlloySmelter), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.AssemblyMachine), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.BlastFurnace), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.centrifuge), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.chargeBench), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.ChemicalReactor), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.ChunkLoader), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.ComputerCube), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_GENERATION_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.DieselGenerator), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.digitalChest), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Distillationtower), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_GENERATION_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Dragoneggenergysiphoner), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.ElectricCraftingTable), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.farm), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.FusionCoil), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.FusionControlComputer), ""));
		pageCollection.addPage(new CraftingInfoPage("BLOCK_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Gasturbine), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Grinder), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_GENERATION_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.heatGenerator), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.HighAdvancedMachineBlock), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_STORAGE_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Idsu), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.ImplosionCompressor), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.IndustrialElectrolyzer), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.lathe), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_STORAGE_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Lesu), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_STORAGE_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.LesuStorage), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_GENERATION_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.LightningRod), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.machineframe), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_GENERATION_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.MagicalAbsorber), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_GENERATION_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Magicenergeyconverter), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.MatterFabricator), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Metalshelf), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.PlasmaGenerator), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.platecuttingmachine), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.quantumChest), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.quantumTank), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.RollingMachine), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_GENERATION_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Semifluidgenerator), ""));
		pageCollection.addPage(new CraftingInfoPage("POWER_GENERATION_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.thermalGenerator), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.VacuumFreezer), ""));
		pageCollection.addPage(new CraftingInfoPage("MACHINES_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModBlocks.Woodenshelf), ""));

		pageCollection.addPage(new CraftingInfoPage("TOOLS_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModItems.advancedDrill), ""));
		pageCollection.addPage(new CraftingInfoPage("TOOLS_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModItems.cloakingDevice), ""));
		pageCollection.addPage(new CraftingInfoPage("TOOLS_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModItems.lapotronicOrb), ""));
		pageCollection.addPage(new CraftingInfoPage("TOOLS_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModItems.lapotronpack), ""));
		pageCollection.addPage(new CraftingInfoPage("TOOLS_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModItems.lithiumBatpack), ""));
		pageCollection.addPage(new CraftingInfoPage("TOOLS_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModItems.omniTool), ""));
		pageCollection.addPage(new CraftingInfoPage("TOOLS_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModItems.rockCutter), ""));
		pageCollection.addPage(new CraftingInfoPage("ITEM_PAGE."+getNextPageIndex(), pageCollection, new ItemStack(ModItems.uuMatter), ""));
		
		return pageCollection;
	}

	private int getNextPageIndex(){
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
	public void mouseMovedOrUp(int par1, int par2, int par3){
		root.mouseMovedOrUp(par1, par2, par3);
	}

	@Override
	public void mouseClicked(int par1, int par2, int par3){
		root.mouseClicked(par1, par2, par3);
	}

	@Override
	public void handleInput() {
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
