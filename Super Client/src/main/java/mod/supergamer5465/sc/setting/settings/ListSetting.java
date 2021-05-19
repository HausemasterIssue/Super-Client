package mod.supergamer5465.sc.setting.settings;

import java.util.List;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;

public class ListSetting extends Setting {
	public List<?> values;

	public ListSetting(String name, Module parent, List<?> values) {
		this.name = name;
		this.parent = parent;
		if (values == null)
			this.values = values;
		this.type = "list";
	}

	public List<?> getValues() {
		return this.values;
	}

	public void setValues(List<?> values) {
		this.values = values;

		if (Main.config != null) {
			Main.config.Save();
		}
	}

	// TODO add different functions for block list, entity list, etc.
}