package mod.supergamer5465.sc.command.commands;

import mod.supergamer5465.sc.Client;
import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.command.Command;
import mod.supergamer5465.sc.module.Module;

public class Toggle extends Command {

	@Override
	public String getAlias() {
		return "toggle";
	}

	@Override
	public String getDescription() {
		return "toggles modules";
	}

	@Override
	public String getSyntax() {
		return ".toggle [Module]";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		for (Module m : Main.moduleManager.getModuleList()) {
			if (args[0].equalsIgnoreCase(m.getName())) {
				m.toggle();
				Client.addChatMessage(args[0] + " has been toggled");
				return;
			}
		}
		Client.addChatMessage("Module " + args[0] + " was not found");
		Client.addChatMessage(this.getSyntax());
	}

}
