package mod.supergamer5465.sc.setting.settings;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;

public class BooleanSetting extends Setting {
	public boolean enabled;

	public BooleanSetting(String name, Module parent, boolean enabled) {
		this.name = name;
		this.parent = parent;
		this.enabled = enabled;
		this.type = "boolean";
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if (Main.config != null) {
			Main.config.Save();
		}
	}

	public void toggled() {
		this.enabled = !this.enabled;

		if (Main.config != null) {
			Main.config.Save();
		}
	}

}