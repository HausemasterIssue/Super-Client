package mod.imphack.module.modules.hud;

import java.awt.Color;

import mod.imphack.Main;
import mod.imphack.module.modules.hud.HudArrayList.ModuleComparator;
import mod.imphack.setting.settings.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HudCoords extends Gui{

	private final Minecraft mc = Minecraft.getMinecraft();
	
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

		if (mc.player.dimension == -1) {
			fr.drawStringWithShadow(round(x) + ", " + round(y) + ", " + round(z), 2,
					(sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, 0xff0000);
			fr.drawStringWithShadow(round(x * 8) + ", " + round(y) + ", " + round(z * 8), 2,
					sr.getScaledHeight() - (fr.FONT_HEIGHT * 2) - 2, 0x00ff00);
		}
		if (mc.player.dimension == 0) {
			fr.drawStringWithShadow(round(x) + ", " + round(y) + ", " + round(z), 2,
					sr.getScaledHeight() - (fr.FONT_HEIGHT) - 2, 0x00ff00);
			fr.drawStringWithShadow(round(x / 8) + ", " + round(y) + ", " + round(z / 8), 2,
					(sr.getScaledHeight() - fr.FONT_HEIGHT * 2) - 2, 0xff0000);
		}
		if (mc.player.dimension == 1) {
			fr.drawStringWithShadow(round(x) + ", " + round(y) + ", " + round(z), 2,
					(sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, 0x800080);
		}
			}
		}
	}
}

	public static String round(double num) {
		return (Integer.valueOf((int) num)).toString() + "."
				+ (Integer.valueOf(Math.abs((int) ((num % 1) * 10)))).toString();
	}

	
	public static int rainbow(int delay) {
		double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 50.0);
		rainbowState %= 360;
		return Color.getHSBColor((float) (rainbowState / 360.0f), 0.5f, 1f).getRGB();
	}
}
