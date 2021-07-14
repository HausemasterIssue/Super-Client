package mod.supergamer5465.sc.event.events;

import mod.supergamer5465.sc.event.ScEventCancellable;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;

public class ScEventSettings extends ScEventCancellable {
	public Setting setting;
	public Module module;

	public ScEventSettings(Setting setting, Module module) {
		super();
		setting = this.setting;
		module = this.module;
	}
}
