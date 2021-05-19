package mod.supergamer5465.sc.setting.settings;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;

public class IntSetting extends Setting {
	public int value;

	public IntSetting(String name, Module parent, int value) {
		this.name = name;
		this.parent = parent;
		if (!Main.configLoaded)
			this.value = value;
		this.type = "int";
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;

		if (Main.config != null) {
			Main.config.Save();
		}
	}
}