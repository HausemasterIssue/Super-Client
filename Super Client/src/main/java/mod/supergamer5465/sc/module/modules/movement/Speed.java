package mod.supergamer5465.sc.module.modules.movement;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import mod.supergamer5465.sc.setting.settings.ModeSetting;

public class Speed extends Module {
	public Speed() {
		super("Speed", "Boosts the default walking speed", Category.MOVEMENT);

		this.addSetting(new ModeSetting("Mode", this, "strafe", new String[] { "strafe", "sprint" }));
		this.addSetting(new FloatSetting("Multiplier", this, 1f));

	}
}
