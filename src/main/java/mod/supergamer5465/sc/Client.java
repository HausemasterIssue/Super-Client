package mod.supergamer5465.sc;

import mod.supergamer5465.sc.command.CommandManager;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.ui.clickgui.ClickGuiController;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

//class to interact with the client

public class Client {

	public static String lastConnectedIP = null;
	public static int lastConnectedPort = -1;

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

	public static volatile boolean getNextKeyPressForKeybinding = false;

	public static Module keybindModule;

	public static void waitForKeybindKeypress(Module m) {
		keybindModule = m;
		getNextKeyPressForKeybinding = true;
	}

	public static void stopWaitForKeybindPress() {
		getNextKeyPressForKeybinding = false;
		keybindModule = null;
		ClickGuiController.INSTANCE.settingController.refresh(false);
	}
}
