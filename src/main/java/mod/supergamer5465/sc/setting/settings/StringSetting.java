package mod.supergamer5465.sc.setting.settings;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;

public class StringSetting extends Setting {
	public String value;

	public StringSetting(String name, Module parent, String value) {
		this.name = name;
		this.parent = parent;
		if (value == null)
			this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;

		if (Main.config != null) {
			Main.config.Save();
		}
	}
}