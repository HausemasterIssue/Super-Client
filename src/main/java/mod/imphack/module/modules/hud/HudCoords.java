package mod.imphack.module.modules.hud;

import java.awt.Color;

import mod.imphack.Main;
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
					final int counter[] = {0};

					double x = mc.player.posX;
					double y = mc.player.posY;
					double z = mc.player.posZ;

					if (mc.player.dimension == -1) {
						fr.drawStringWithShadow("Overworld: ", 2, sr.getScaledHeight() - (fr.FONT_HEIGHT * 2) - 2, rainbow(counter[0]  * 300));
						fr.drawStringWithShadow("Nether: ", 2, sr.getScaledHeight() - (fr.FONT_HEIGHT ) - 2 , rainbow(counter[0]  * 300));

						fr.drawStringWithShadow(round(x) + ", " + round(y) + ", " + round(z), 42,
								(sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, rainbow(counter[0]  * 300));
						fr.drawStringWithShadow(round(x * 8) + ", " + round(y) + ", " + round(z * 8), 62,
								sr.getScaledHeight() - (fr.FONT_HEIGHT * 2) - 2, rainbow(counter[0]  * 300));
					}
					if (mc.player.dimension == 0) {
						fr.drawStringWithShadow("Overworld: ", 2, sr.getScaledHeight() - (fr.FONT_HEIGHT) - 2, rainbow(counter[0]  * 300));
						fr.drawStringWithShadow(round(x) + ", " + round(y) + ", " + round(z), 62,
								sr.getScaledHeight() - (fr.FONT_HEIGHT) - 2, rainbow(counter[0]  * 300));
						
						fr.drawStringWithShadow("Nether: ", 2, sr.getScaledHeight() - (fr.FONT_HEIGHT * 2) - 2 , rainbow(counter[0]  * 300));
						fr.drawStringWithShadow(round(x / 8) + ", " + round(y) + ", " + round(z / 8), 42,
								(sr.getScaledHeight() - fr.FONT_HEIGHT * 2) - 2, rainbow(counter[0]  * 300));
					}
					if (mc.player.dimension == 1) {
						fr.drawStringWithShadow("Nether: ", 2, sr.getScaledHeight() - (fr.FONT_HEIGHT ) - 2 , rainbow(counter[0]  * 300));

						fr.drawStringWithShadow(round(x) + ", " + round(y) + ", " + round(z), 42,
								(sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, rainbow(counter[0]  * 300));
						counter[0]++;
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
