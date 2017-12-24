package techreborn.client.gui.slot.elements;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.FMLClientHandler;
import reborncore.RebornCore;
import reborncore.client.gui.GuiUtil;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.packet.PacketIOSave;
import reborncore.common.network.packet.PacketSlotSave;
import reborncore.common.tile.SlotConfiguration;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.MachineFacing;
import techreborn.client.gui.GuiBase;

import java.awt.*;

public class SlotConfigPopupElement extends ElementBase {
	int id;

	ConfigSlotElement slotElement;

	public SlotConfigPopupElement(int slotId, int x, int y, ConfigSlotElement slotElement) {
		super(x, y, Sprite.SLOT_CONFIG_POPUP);
		this.id = slotId;
		this.slotElement = slotElement;
	}

	@Override
	public void draw(GuiBase gui) {

		super.draw(gui);
		TileLegacyMachineBase machine = ((TileLegacyMachineBase) gui.tile);
		IBlockAccess blockAccess = machine.getWorld();
		BlockPos pos = machine.getPos();
		IBlockState state = blockAccess.getBlockState(pos);
		IBlockState actualState = Blocks.DIRT.getDefaultState().getActualState(blockAccess, pos);
		BlockRendererDispatcher dispatcher = FMLClientHandler.instance().getClient().getBlockRendererDispatcher();
		IBakedModel model = dispatcher.getBlockModelShapes().getModelForState(state.withProperty(BlockMachineBase.FACING, EnumFacing.NORTH));
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 4, 23); //left
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 23, -12, -90F, 1F, 0F, 0F); //top
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 23, 23, -90F, 0F, 1F, 0F); //centre
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 23, 42, 90F, 1F, 0F, 0F); //bottom
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 26, 23, 180F, 0F, 1F, 0F); //right
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, 26, 42, 90F, 0F, 1F, 0F); //back


		drawSlotSateColor(gui.getMachine(), MachineFacing.UP.getFacing(machine), id, 22, -1, gui);
		drawSlotSateColor(gui.getMachine(), MachineFacing.FRONT.getFacing(machine), id, 22, 18, gui);
		drawSlotSateColor(gui.getMachine(), MachineFacing.DOWN.getFacing(machine), id, 22, 37, gui);
		drawSlotSateColor(gui.getMachine(), MachineFacing.RIGHT.getFacing(machine), id, 41, 18, gui);
		drawSlotSateColor(gui.getMachine(), MachineFacing.BACK.getFacing(machine), id, 41, 37, gui);
		drawSlotSateColor(gui.getMachine(), MachineFacing.LEFT.getFacing(machine), id, 3, 18, gui);
	}

	@Override
	public boolean onRelease(TileLegacyMachineBase provider, GuiBase gui, int mouseX, int mouseY) {
		int mx = mouseX - getX() - gui.guiLeft;
		int my = mouseY - getY() - gui.guiTop;
		if(isInBox(147 , 40, 20, 20, mx, my)){
			cyleSlotConfig(MachineFacing.UP.getFacing(provider), gui);
		} else if(isInBox(147 , 60, 20, 20, mx, my)){
			cyleSlotConfig(MachineFacing.FRONT.getFacing(provider), gui);
		} else if(isInBox(128 , 60, 20, 20, mx, my)){
			cyleSlotConfig(MachineFacing.LEFT.getFacing(provider), gui);
		} else if(isInBox(166 , 60, 20, 20, mx, my)){
			cyleSlotConfig(MachineFacing.RIGHT.getFacing(provider), gui);
		} else if(isInBox(147 , 80, 20, 20, mx, my)){
			cyleSlotConfig(MachineFacing.DOWN.getFacing(provider), gui);
		} else if(isInBox(166 , 80, 20, 20, mx, my)){
			cyleSlotConfig(MachineFacing.BACK.getFacing(provider), gui);
		} else {
			return false;
		}
		return true;
	}

	public void cyleSlotConfig(EnumFacing side, GuiBase guiBase){
		SlotConfiguration.SlotConfig currentSlot = guiBase.getMachine().slotConfiguration.getSlotDetails(id).getSideDetail(side);

		SlotConfiguration.SlotIO slotIO = new SlotConfiguration.SlotIO(currentSlot.getSlotIO().getIoConfig().getNext());
		SlotConfiguration.SlotConfig newConfig = new SlotConfiguration.SlotConfig(side, slotIO, id);
		PacketSlotSave packetSlotSave = new PacketSlotSave(guiBase.tile.getPos(), newConfig);
		NetworkManager.sendToServer(packetSlotSave);
	}

	public void updateCheckBox(CheckBoxElement checkBoxElement, String type, GuiBase guiBase){
		SlotConfiguration.SlotConfigHolder configHolder = guiBase.getMachine().slotConfiguration.getSlotDetails(id);
		boolean input = configHolder.autoInput();
		boolean output = configHolder.autoOutput();
		if(type.equalsIgnoreCase("input")){
			input = !configHolder.autoInput();
		}
		if(type.equalsIgnoreCase("output")){
			output = !configHolder.autoOutput();
		}

		PacketIOSave packetSlotSave = new PacketIOSave(guiBase.tile.getPos(), id, input, output);
		NetworkManager.sendToServer(packetSlotSave);
	}

	private void drawSlotSateColor(TileLegacyMachineBase machineBase, EnumFacing side, int slotID, int inx, int iny, GuiBase gui){
		iny += 4;
		int sx = inx + getX() + gui.guiLeft;
		int sy = iny + getY() + gui.guiTop;
		SlotConfiguration.SlotConfigHolder slotConfigHolder = machineBase.slotConfiguration.getSlotDetails(slotID);
		if(slotConfigHolder == null){
			RebornCore.logHelper.debug("Humm, this isnt suppoed to happen");
			return;
		}
		SlotConfiguration.SlotConfig slotConfig = slotConfigHolder.getSideDetail(side);
		Color color;
		switch (slotConfig.getSlotIO().getIoConfig()){
			case NONE:
				color  = new Color(0, 0, 0, 0);
				break;
			case INPUT:
				color  = new Color(0, 0, 255, 128);
				break;
			case OUTPUT:
				color  = new Color(255, 69, 0, 128);
				break;
			default:
				color = new Color(0, 0, 0, 0);
				break;
		}
		GlStateManager.color(255, 255, 255);
		GuiUtil.drawGradientRect(sx, sy, 18, 18, color.getRGB(), color.getRGB());
		GlStateManager.color(255, 255, 255);

	}

	private boolean isInBox(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY){
		return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
	}

	public void drawState(GuiBase gui,
	                      IBlockAccess blockAccess,
	                      IBakedModel model,
	                      IBlockState actualState,
	                      BlockPos pos,
	                      BlockRendererDispatcher dispatcher,
	                      int x,
	                      int y,
	                      float rotAngle,
	                      float rotX,
	                      float rotY,
	                      float rotZ) {

		GlStateManager.pushMatrix();
		GlStateManager.enableDepth();
		GlStateManager.translate(8 + gui.guiLeft + this.x + x, 8 + gui.guiTop + this.y + y, 512);
		GlStateManager.scale(16F, 16F, 16F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.scale(-1, -1, -1);
		if (rotAngle != 0) {
			GlStateManager.rotate(rotAngle, rotX, rotY, rotZ);
		}
		dispatcher.getBlockModelRenderer().renderModelBrightness(model, actualState, 1F, false);
		GlStateManager.disableDepth();
		GlStateManager.popMatrix();

/*		GlStateManager.pushMatrix();
		GlStateManager.enableDepth();
		//		GlStateManager.translate(8 + gui.xFactor + this.x + x, 8 + gui.yFactor + this.y + y, 1000);
		GlStateManager.translate(gui.xFactor + this.x + x, gui.yFactor + this.y + y, 512);
		if (rotAngle != 0) {
			GlStateManager.rotate(rotAngle, rotX, rotY, rotZ);
		}
		GlStateManager.scale(16F, 16F, 16F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		GlStateManager.scale(-1, -1, -1);
		GlStateManager.disableDepth();
		GlStateManager.popMatrix();*/
	}

	public void drawState(GuiBase gui, IBlockAccess blockAccess, IBakedModel model, IBlockState actualState, BlockPos pos, BlockRendererDispatcher dispatcher, int x, int y) {
		drawState(gui, blockAccess, model, actualState, pos, dispatcher, x, y, 0, 0, 0, 0);
	}
}