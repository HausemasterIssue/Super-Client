package mod.supergamer5465.sc.module.modules.movement;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import mod.supergamer5465.sc.setting.settings.ModeSetting;
import net.minecraft.util.math.MathHelper;

public class Speed extends Module {

	ModeSetting mode = new ModeSetting("Mode", this, "strafe", new String[] { "strafe", "sprint" });
	FloatSetting multiplier = new FloatSetting("Multiplier", this, 1f);

	public Speed() {
		super("Speed", "Boosts the default walking speed", Category.MOVEMENT);

		this.addSetting(mode);
		this.addSetting(multiplier);

	}

	@Override
	public void onUpdate() {
		if (mode.getMode().equalsIgnoreCase("sprint")) {
			if (mc.player.movementInput.moveForward != 0 && !mc.player.isSneaking()
					|| mc.player.movementInput.moveStrafe != 0 && !mc.player.isSneaking())
				mc.player.setSprinting(true);
		}
		if (mode.getMode().equalsIgnoreCase("strafe")) {
			if ((mc.player.moveForward != 0 || mc.player.moveStrafing != 0) && !mc.player.isSneaking()) {
				final float yaw = mc.player.rotationYaw * 0.017453292F;
				mc.player.motionX -= MathHelper.sin(yaw) * 0.2f;
				mc.player.motionZ += MathHelper.cos(yaw) * 0.2f;// TODO test
			}
		}
	}
}
