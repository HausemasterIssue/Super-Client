package mod.imphack.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class ImpHackDiscordRichPresence {
	private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
	private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

	public static void start() {
		DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
		eventHandlers.disconnected = ((var1, var2) -> System.out
				.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

		String discordID = "870753739500290060";
		discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

		discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
		discordRichPresence.details = Reference.NAME + " " + Reference.VERSION;
		discordRichPresence.largeImageKey = "2b2t_-_spawnbase_2b2t-891";
		discordRichPresence.smallImageKey = "skyrim";
		discordRichPresence.state = null;

		discordRPC.Discord_UpdatePresence(discordRichPresence);
	}

	public static void stop() {
		discordRPC.Discord_Shutdown();
		discordRPC.Discord_ClearPresence();
	}
}
