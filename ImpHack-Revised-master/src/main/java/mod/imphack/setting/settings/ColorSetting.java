package mod.imphack.setting.settings;

import mod.imphack.Main;
import mod.imphack.module.Module;
import mod.imphack.setting.Setting;

public class ColorSetting extends Setting {
	public int red;
	public int green;
	public int blue;

	public ColorSetting(String name, Module parent, int red, int green, int blue) {
		this.name = name;
		this.parent = parent;
		if (!Main.configLoaded) {
			this.red = red;
			this.green = green;
			this.blue = blue;
		}
	}
}
