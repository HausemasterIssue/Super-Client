package mod.imphack.module.modules.client;

import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.util.ImpHackDiscordRichPresence;

public class DiscordRPC extends Module {

	public DiscordRPC() {
		super("DiscordRPC", "Rich Presence For Discord", Category.CLIENT);
	}

	@Override
	public void onEnable() {
		ImpHackDiscordRichPresence.start();
	}

	@Override
	public void onDisable() {
		ImpHackDiscordRichPresence.stop();
	}
}
