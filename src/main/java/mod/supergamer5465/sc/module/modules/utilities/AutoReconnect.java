package mod.supergamer5465.sc.module.modules.utilities;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.IntSetting;

public class AutoReconnect extends Module {

	public IntSetting timer = new IntSetting("Timer", this, 5000);

	public AutoReconnect() {
		super("AutoReconnect", "Reconnects You Automatically", Category.UTILITIES);

		addSetting(timer);
	}
}
