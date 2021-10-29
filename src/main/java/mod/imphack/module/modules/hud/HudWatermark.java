package mod.imphack.module.modules.hud;

import java.awt.Font;

import mod.imphack.Main;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HudWatermark {

	private final Minecraft mc = Minecraft.getMinecraft();



	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event) {
		if (Main.moduleManager.getModule("Hud").toggled) {
			FontRenderer fr = mc.fontRenderer;

			if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
				if (((BooleanSetting) Main.settingManager.getSettingByName(Main.moduleManager.getModule("Hud"),
						"Watermark")).enabled)
					mc.fontRenderer.drawStringWithShadow("ImpHack Revised " + Reference.VERSION, 2, 1, 0xa600ff);

			}
		}
	}
}
