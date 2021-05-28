package mod.supergamer5465.sc.module.modules.utilities;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;

public class AutoFish extends Module {

	BooleanSetting findRod = new BooleanSetting("Inventory Rod Finder", this, true);
	BooleanSetting autoRepair = new BooleanSetting("Offhand Mending Repair", this, true);

	public AutoFish() {
		super("AutoFish", "Fishes Automatically", Category.UTILITIES);

		addSetting(findRod);
		addSetting(autoRepair);
	}

	@Override
	public void onUpdate() {
	}
}
