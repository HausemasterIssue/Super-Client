package mod.imphack.module.modules.combat;

import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.setting.settings.FloatSetting;

public class Anchor extends Module {

	final FloatSetting downSpeed = new FloatSetting("DownSpeed", this, 0.15f);
	final FloatSetting fallHeight = new FloatSetting("FallHeight", this, 2.0f);
	final BooleanSetting stopMovement = new BooleanSetting("StopMovement", this, true);

	public Anchor() {
		super("Anchor", "Get in to holes quicker", Category.COMBAT);

		this.addSetting(downSpeed);
		this.addSetting(fallHeight);
		this.addSetting(stopMovement);
	}

	@Override
	public void onUpdate() {
		if (mc.player.fallDistance > fallHeight.getValue()) {
			mc.player.motionY -= downSpeed.getValue();
		}

		if (stopMovement.isEnabled() && mc.player.fallDistance > fallHeight.getValue())
			mc.player.setSprinting(false);
	}
}