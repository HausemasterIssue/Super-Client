package mod.supergamer5465.sc.module.modules.client;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.util.ScDiscordRichPresence;

public class DiscordRPC extends Module {

	public DiscordRPC() {
		super("DiscordRPC", "Rich Presence For Discord", Category.CLIENT);
	}

	@Override
	public void onEnable() {
		ScDiscordRichPresence.start();
	}

	@Override
	public void onDisable() {
		ScDiscordRichPresence.stop();
	}
}
