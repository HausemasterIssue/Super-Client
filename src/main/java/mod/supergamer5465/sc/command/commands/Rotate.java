package mod.supergamer5465.sc.command.commands;

import mod.supergamer5465.sc.Client;
import mod.supergamer5465.sc.command.Command;
import net.minecraft.client.Minecraft;

public class Rotate extends Command {

	@Override
	public String getAlias() {
		return "rotate";
	}

	@Override
	public String getDescription() {
		return "Sets the player rotation";
	}

	@Override
	public String getSyntax() {
		return ".rotate [pitch] [yaw]";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if (args[0].isEmpty() || args[1].isEmpty()) {
			Client.addChatMessage("No arguments found");
			Client.addChatMessage(this.getSyntax());
		} else {
			Minecraft.getMinecraft().player.rotationPitch = Float.valueOf(args[0]);
			Minecraft.getMinecraft().player.rotationYaw = Float.valueOf(args[1]);
			Minecraft.getMinecraft().player.rotationYawHead = Float.valueOf(args[1]);
			Client.addChatMessage("Rotated player");
		}

	}

}
