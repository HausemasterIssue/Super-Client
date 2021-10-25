package mod.imphack.module.modules.hud;

import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;

public class Hud extends Module {

	BooleanSetting watermark = new BooleanSetting("Watermark", this, true);
	BooleanSetting arraylist = new BooleanSetting("ArrayList", this, true);
	BooleanSetting coords = new BooleanSetting("Coordinates", this, true);

	public Hud() {
		super("Hud", "In-Game Overlay", Category.HUD);

		addSetting(watermark);
		addSetting(arraylist);
		addSetting(coords);
	}
}
