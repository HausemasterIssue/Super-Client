package mod.supergamer5465.sc.setting.settings;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;

public class FloatSetting extends Setting {
	public float value;

	public FloatSetting(String name, Module parent, Float value) {
		this.name = name;
		this.parent = parent;
		if (!Main.configLoaded)
			this.value = value;
	}

	public float getValue() {
		return this.value;
	}

	public void setValue(float value) {
		this.value = value;

		if (Main.config != null) {
			Main.config.Save();
		}
	}
}