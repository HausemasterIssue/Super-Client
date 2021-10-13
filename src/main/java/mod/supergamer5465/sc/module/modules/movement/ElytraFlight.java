package mod.supergamer5465.sc.module.modules.movement;

import mod.supergamer5465.sc.event.events.ScUpdateEvent;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class ElytraFlight extends Module {

	FloatSetting downSpeed = new FloatSetting("DownSpeed", this, 0.15f);
	FloatSetting upSpeed = new FloatSetting("UpSpeed", this, 2.0f);
	FloatSetting baseSpeed = new FloatSetting("BaseSpeed", this, 0.15f);
	BooleanSetting noVelocity = new BooleanSetting("noVelocity", this, true);

	public ElytraFlight() {
		super("ElytraFlight", "Fly with Elytras", Category.MOVEMENT);

		this.addSetting(downSpeed);
		this.addSetting(upSpeed);
		this.addSetting(baseSpeed);
		this.addSetting(noVelocity);
	}

	@Override
	public void onUpdate() {

		if (!Minecraft.getMinecraft().player.isElytraFlying()) return;

		float yaw = Minecraft.getMinecraft().player.rotationYaw;
		float pitch = Minecraft.getMinecraft().player.rotationPitch;

		if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
			Minecraft.getMinecraft().player.motionX -= Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * baseSpeed.getValue();
			Minecraft.getMinecraft().player.motionZ += Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * baseSpeed.getValue();
		}
		if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown()) {
			Minecraft.getMinecraft().player.motionY += upSpeed.getValue();
		}
		if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
			Minecraft.getMinecraft().player.motionY -= downSpeed.getValue();
		}

		if (noVelocity.isEnabled())
			if (!Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown() && !Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && !Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.setVelocity(0, 0, 0);
			}
	}
}
