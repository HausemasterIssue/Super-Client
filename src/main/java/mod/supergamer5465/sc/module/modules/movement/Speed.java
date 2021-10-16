package mod.supergamer5465.sc.module.modules.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.supergamer5465.sc.event.events.ScEventMove;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import mod.supergamer5465.sc.setting.settings.ModeSetting;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;

public class Speed extends Module {

	ModeSetting mode = new ModeSetting("Mode", this, "strafe", new String[] { "strafe" });
	FloatSetting multiplier = new FloatSetting("Multiplier", this, 1f);
	BooleanSetting inLiquid = new BooleanSetting("While In Liquids", this, true);
	BooleanSetting jump = new BooleanSetting("Auto Jump", this, false);

	public Speed() {
		super("Speed", "Boosts the default walking speed", Category.MOVEMENT);

		this.addSetting(mode);
		this.addSetting(multiplier);
		this.addSetting(inLiquid);
		this.addSetting(jump);

	}

	@Override
	public void onUpdate() {
		if (mc.player.isInWater() || mc.player.isInLava()) {
			if (!inLiquid.enabled)
				return;
		}
		if (mode.getMode().equalsIgnoreCase("strafe")) {
			if (mc.player.isRiding())
				return;

			final float yaw = YawRotationUtility();
			if (mc.player.movementInput.backKeyDown || mc.player.movementInput.forwardKeyDown
					|| mc.player.movementInput.leftKeyDown || mc.player.movementInput.rightKeyDown) {
				if (jump.enabled || mc.player.onGround) {
					mc.player.motionX -= MathHelper.sin(yaw) * 0.017453292f * multiplier.value;
					mc.player.motionZ += MathHelper.cos(yaw) * 0.017453292f * multiplier.value;
				}
			}

			if (mc.gameSettings.keyBindJump.isKeyDown() && mc.player.onGround
					|| mc.player.onGround && jump.enabled && (mc.player.motionX != 0 || mc.player.motionZ != 0)) {
				mc.player.motionY = 0.405f;
			}
		}
	}

	@EventHandler
	private Listener<ScEventMove> player_move = new Listener<>(event -> {
		if (mc.player.isInWater() || mc.player.isInLava()) {
			if (!inLiquid.enabled)
				return;
		}

		if (mc.player.isSneaking() || mc.player.isOnLadder() || mc.player.isInLava() || mc.player.isInWater()
				|| mc.player.capabilities.isFlying)
			return;

		float player_speed = 0.2873f * multiplier.value;
		float move_forward = mc.player.movementInput.moveForward;
		float move_strafe = mc.player.movementInput.moveStrafe;
		float rotation_yaw = mc.player.rotationYaw;

		if (mc.player.isPotionActive(MobEffects.SPEED)) {
			final int amp = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
			player_speed *= (1.2f * (amp + 1));
		}

		if (move_forward == 0 && move_strafe == 0) {
			event.set_x(0.0d);
			event.set_z(0.0d);
		} else {
			if (move_forward != 0.0f) {
				if (move_strafe > 0.0f) {
					rotation_yaw += ((move_forward > 0.0f) ? -45 : 45);
				} else if (move_strafe < 0.0f) {
					rotation_yaw += ((move_forward > 0.0f) ? 45 : -45);
				}
				move_strafe = 0.0f;
				if (move_forward > 0.0f) {
					move_forward = 1.0f;
				} else if (move_forward < 0.0f) {
					move_forward = -1.0f;
				}
			}

			event.set_x((move_forward * player_speed) * Math.cos(Math.toRadians((rotation_yaw + 90.0f)))
					+ (move_strafe * player_speed) * Math.sin(Math.toRadians((rotation_yaw + 90.0f))));
			event.set_z((move_forward * player_speed) * Math.sin(Math.toRadians((rotation_yaw + 90.0f)))
					- (move_strafe * player_speed) * Math.cos(Math.toRadians((rotation_yaw + 90.0f))));

		}

		event.cancel();

	});

	private float YawRotationUtility() {
		float rotationYaw = mc.player.rotationYaw;
		if (mc.player.moveForward < 0.0f) {
			rotationYaw += 180.0f;
		}
		float n = 1.0f;
		if (mc.player.moveForward < 0.0f) {
			n = -0.5f;
		} else if (mc.player.moveForward > 0.0f) {
			n = 0.5f;
		}
		if (mc.player.moveStrafing > 0.0f) {
			rotationYaw -= 90.0f * n;
		}
		if (mc.player.moveStrafing < 0.0f) {
			rotationYaw += 90.0f * n;
		}
		return rotationYaw * 0.017453292f;
	}
}
