package mod.imphack.module.modules.hud;

import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;

public class Hud extends Module {

	final BooleanSetting watermark = new BooleanSetting("Watermark", this, true);
	final BooleanSetting arraylist = new BooleanSetting("ArrayList", this, true);
	final BooleanSetting coords = new BooleanSetting("Coordinates", this, true);
	final BooleanSetting fps = new BooleanSetting("FPS", this, true);
	final BooleanSetting armor = new BooleanSetting("Armor", this, true);


	public Hud() {
		super("Hud", "In-Game Overlay", Category.HUD);

		addSetting(watermark);
		addSetting(arraylist);
		addSetting(coords);
		addSetting(fps);
		addSetting(armor);

	}
}
