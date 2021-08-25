package mod.supergamer5465.sc.module.modules.utilities;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import net.minecraft.client.multiplayer.ServerData;

public class Reconnect extends Module {

	public IntSetting timer = new IntSetting("Timer", this, 5000);

	public ServerData serverData;

	public Reconnect() {
		super("Reconnect", "Reconnects You Automatically", Category.UTILITIES);

		addSetting(timer);
	}
}
