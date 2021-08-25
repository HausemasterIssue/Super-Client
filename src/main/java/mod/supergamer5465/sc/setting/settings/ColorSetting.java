package mod.supergamer5465.sc.setting.settings;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;

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
