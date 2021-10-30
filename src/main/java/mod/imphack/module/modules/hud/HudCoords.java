package mod.imphack.module.modules.hud;

import java.awt.Color;
import java.awt.Font;

import mod.imphack.Main;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.util.font.FontUtils;
import mod.imphack.util.render.ColorUtil;
import mod.imphack.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HudCoords extends Gui{
	
	private final Minecraft mc = Minecraft.getMinecraft();
	FontRenderer fr = mc.fontRenderer;


	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
		if (Main.moduleManager.getModule("Hud").toggled) {
			ScaledResolution sr = new ScaledResolution(mc);
			FontRenderer fr = mc.fontRenderer;
		
			if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
				if (((BooleanSetting) Main.settingManager.getSettingByName(Main.moduleManager.getModule("Hud"),
						"Coordinates")).enabled) {
				
				double x = mc.player.posX;
				double y = mc.player.posY;
				double z = mc.player.posZ;
				
				String coords = String.format("X: %s Y: %s Z: %s", RenderUtil.DF((float)x, 1), RenderUtil.DF((int) y, 1), RenderUtil.DF((float)z, 1));
				boolean isChatOpen = mc.currentScreen instanceof GuiChat;
				
				int heightCoords = isChatOpen ? sr.getScaledHeight() - 25 : sr.getScaledHeight() - 10;
				
				mc.fontRenderer.drawStringWithShadow(coords,  2, heightCoords, ColorUtil.rainbow(300));
				}
			}
		}
	}
}
