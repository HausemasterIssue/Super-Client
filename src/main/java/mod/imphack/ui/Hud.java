package mod.imphack.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mod.imphack.Main;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Hud extends Gui {

	private Minecraft mc = Minecraft.getMinecraft();

	public static class ModuleComparator implements Comparator<Module> {

		@Override
		public int compare(Module arg0, Module arg1) {
			if (Minecraft.getMinecraft().fontRenderer.getStringWidth(
					arg0.getName()) > Minecraft.getMinecraft().fontRenderer.getStringWidth(arg1.getName())) {
				return -1;
			}
			if (Minecraft.getMinecraft().fontRenderer.getStringWidth(
					arg0.getName()) > Minecraft.getMinecraft().fontRenderer.getStringWidth(arg1.getName())) {
				return 1;
			}
			return 0;
		}
	}

	final Comparator<Module> comparator = (a, b) -> {
		final String firstName = a.getName();
		final String secondName = b.getName();
		final float dif = Minecraft.getMinecraft().fontRenderer.getStringWidth(secondName)
				- Minecraft.getMinecraft().fontRenderer.getStringWidth(firstName);
		return dif != 0 ? (int) dif : secondName.compareTo(firstName);
	};

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
		if (Main.moduleManager.getModule("Hud").toggled) {
			Collections.sort(Main.moduleManager.modules, new ModuleComparator());
			ScaledResolution sr = new ScaledResolution(mc);
			FontRenderer fr = mc.fontRenderer;

			if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
				if (((BooleanSetting) Main.settingManager.getSettingByName(Main.moduleManager.getModule("Hud"),
						"Watermark")).enabled)
					fr.drawStringWithShadow("ImpHack Revised " + Reference.VERSION, 2, 1, 0xa600ff);

				if (((BooleanSetting) Main.settingManager.getSettingByName(Main.moduleManager.getModule("Hud"),
						"ArrayList")).enabled) {
					int y = 2;
					ArrayList<Module> modules = new ArrayList<>();
					for (Module mod : Main.moduleManager.getModuleList()) {
						if (!mod.getName().equalsIgnoreCase("") && mod.isToggled()) {
							modules.add(mod);
						}
					}

					modules.sort(comparator);

					for (int i = 0; i < modules.size(); i++) {
						Module m = modules.get(i);
						fr.drawStringWithShadow(m.getName(), sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 2,
								y, rainbow(i * 300));
						y += fr.FONT_HEIGHT;
					}
				}
				if (((BooleanSetting) Main.settingManager.getSettingByName(Main.moduleManager.getModule("Hud"),
						"Coordinates")).enabled) {

					String x;
					String y;
					String z;

					if (((BooleanSetting) Main.settingManager.getSettingByName(Main.moduleManager.getModule("Hud"),
							"Round Coordinates")).enabled) {
						x = Integer.toString((int) (mc.player.posX));
						y = Integer.toString((int) (mc.player.posY));
						z = Integer.toString((int) (mc.player.posZ));
					} else {
						x = Double.toString(mc.player.posX);
						y = Double.toString(mc.player.posY);
						z = Double.toString(mc.player.posZ);
					}

					if (((BooleanSetting) Main.settingManager.getSettingByName(Main.moduleManager.getModule("Hud"),
							"Round Coordinates")).enabled) {
						if (mc.player.dimension == -1) {
							fr.drawStringWithShadow(x + ", " + y + ", " + z, 2,
									(sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, 0xff0000);
							fr.drawStringWithShadow(Integer.valueOf(x) * 8 + ", " + y + ", " + Integer.valueOf(z) * 8,
									2, sr.getScaledHeight() - (fr.FONT_HEIGHT * 2) - 2, 0x00ff00);
						}
						if (mc.player.dimension == 0) {
							fr.drawStringWithShadow(Integer.valueOf(x) / 8 + ", " + y + ", " + Integer.valueOf(z) / 8,
									2, (sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, 0xff0000);
							fr.drawStringWithShadow(x + ", " + y + ", " + z, 2,
									sr.getScaledHeight() - (fr.FONT_HEIGHT * 2) - 2, 0x00ff00);
						}
						if (mc.player.dimension == 1) {
							fr.drawStringWithShadow(x + ", " + y + ", " + z, 2,
									(sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, 0x800080);
						}
					} else {
						if (mc.player.dimension == -1) {
							fr.drawStringWithShadow(x + ", " + y + ", " + z, 2,
									(sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, 0xff0000);
							fr.drawStringWithShadow(Double.valueOf(x) * 8 + ", " + y + ", " + Double.valueOf(z) * 8, 2,
									sr.getScaledHeight() - (fr.FONT_HEIGHT * 2) - 2, 0x00ff00);
						}
						if (mc.player.dimension == 0) {
							fr.drawStringWithShadow(Double.valueOf(x) / 8 + ", " + y + ", " + Double.valueOf(z) / 8, 2,
									(sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, 0xff0000);
							fr.drawStringWithShadow(x + ", " + y + ", " + z, 2,
									sr.getScaledHeight() - (fr.FONT_HEIGHT * 2) - 2, 0x00ff00);
						}
						if (mc.player.dimension == 1) {
							fr.drawStringWithShadow(x + ", " + y + ", " + z, 2,
									(sr.getScaledHeight() - fr.FONT_HEIGHT) - 2, 0x800080);
						}
					}
				}
			}
		}
	}

	public static int rainbow(int delay) {
		double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 50.0);
		rainbowState %= 360;
		return Color.getHSBColor((float) (rainbowState / 360.0f), 0.5f, 1f).getRGB();
	}
}
