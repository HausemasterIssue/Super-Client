package mod.supergamer5465.sc.module.modules.client;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;

public class Hud extends Module {

	BooleanSetting watermark = new BooleanSetting("Watermark", this, true);
	BooleanSetting arraylist = new BooleanSetting("ArrayList", this, true);
	BooleanSetting coords = new BooleanSetting("Coordinates", this, true);
	BooleanSetting roundCoords = new BooleanSetting("Round Coordinates", this, true);

	public Hud() {
		super("Hud", "In-Game Overlay", Category.CLIENT);

		addSetting(roundCoords);
		addSetting(watermark);
		addSetting(arraylist);
		addSetting(coords);
	}
}
