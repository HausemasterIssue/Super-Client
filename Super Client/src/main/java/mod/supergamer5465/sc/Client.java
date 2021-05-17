package mod.supergamer5465.sc;

import mod.supergamer5465.sc.command.CommandManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

//class to interact with the client

public class Client {

	public static String commandPrefix = ".";

	public static String getCommandPrefix() {
		return commandPrefix;
	}

	public Client() {
		new CommandManager();
	}

	public static void addChatMessage(String s, boolean doPrefixture) {
		String prefixture;
		if (doPrefixture) {
			prefixture = "\247b[Super Client]: ";
		} else {
			prefixture = "";
		}
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString(prefixture + s));
	}

	public static void addChatMessage(String s) {
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString("\247b[Super Client]: " + s));
	}
}
