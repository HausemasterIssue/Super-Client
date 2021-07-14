package mod.supergamer5465.sc.command;

import java.util.ArrayList;

import mod.supergamer5465.sc.Client;
import mod.supergamer5465.sc.command.commands.Bind;
import mod.supergamer5465.sc.command.commands.Help;
import mod.supergamer5465.sc.command.commands.Rotate;
import mod.supergamer5465.sc.command.commands.Toggle;

public class CommandManager {
	private ArrayList<Command> commands;

	public CommandManager() {
		commands = new ArrayList<Command>();

		// add commands
		addCommand(new Bind());
		addCommand(new Toggle());
		addCommand(new Help());
		addCommand(new Rotate());
	}

	public void addCommand(Command c) {
		commands.add(c);
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

	public void callCommand(String input) {

		String[] split = input.split(" ");
		String command = split[0];
		String args = input.substring(command.length()).trim();

		for (Command c : getCommands()) {
			if (c.getAlias().equalsIgnoreCase(command)) {
				try {
					c.onCommand(args, args.split(" "));
				} catch (Exception e) {
					Client.addChatMessage("Invalid command usage");
					Client.addChatMessage(c.getSyntax());
				}
				return;
			}
		}
		Client.addChatMessage("no such command exists");
	}
}
