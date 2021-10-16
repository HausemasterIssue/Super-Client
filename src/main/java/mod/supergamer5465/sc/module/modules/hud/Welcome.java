package mod.supergamer5465.sc.module.modules.hud;

import mod.supergamer5465.sc.setting.settings.FloatSetting;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Welcome extends Module {

    FloatSetting valueX = new FloatSetting("ValueX", this, 0.15f);
    FloatSetting valueY = new FloatSetting("ValueX", this, 0.15f);

    public Welcome() {
        super("Welcomer", "Puts a nice welcome message for u", Category.HUD);

        this.addSetting(valueX);
        this.addSetting(valueY);
    }

    @SubscribeEvent
    public void onRenderGUI(RenderGameOverlayEvent.Post event) {

        int textColor;

        textColor = 0xFF0000;

        GL11.glPushMatrix();
        GL11.glScaled(1.33333333, 1.33333333, 1);
        Minecraft.getMinecraft().ingameGUI.getFontRenderer().drawStringWithShadow(
                "Welcome" + " " + Name(), valueX.getValue(), valueY.getValue(), textColor);
        GL11.glPopMatrix();

    }

    public static String Name() {
        return Minecraft.getMinecraft().player.getName();
    }
}