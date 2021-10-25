package mod.imphack.command;

import java.util.ArrayList;

import mod.imphack.Client;
import mod.imphack.command.commands.Bind;
import mod.imphack.command.commands.Help;
import mod.imphack.command.commands.Rotate;
import mod.imphack.command.commands.Toggle;

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
