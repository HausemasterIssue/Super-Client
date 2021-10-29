package mod.imphack.module.modules.hud;

import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.FloatSetting;
import mod.imphack.util.font.FontUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Font;

import org.lwjgl.opengl.GL11;

public class Welcome extends Module {

	final FloatSetting valueX = new FloatSetting("ValueX", this, 0.15f);
	final FloatSetting valueY = new FloatSetting("ValueY", this, 0.15f);
	
	public static FontUtils font = new FontUtils("Confortaa", Font.PLAIN, 15);

	public Welcome() {
		super("Welcomer", "Puts a nice welcome message for you", Category.HUD);

		this.addSetting(valueX);
		this.addSetting(valueY);
	}

	@SubscribeEvent
	public void onRenderGUI(RenderGameOverlayEvent.Text event) {

		int textColor;

		textColor = 0xFF0000;

		GL11.glPushMatrix();
		GL11.glScaled(1.33333333, 1.33333333, 1);
		font.drawStringWithShadow("Welcome" + " " + Name(),
				valueX.getValue(), valueY.getValue(), textColor);
		GL11.glPopMatrix();

	}

	public static String Name() {
		return Minecraft.getMinecraft().player.getName();
	}
}