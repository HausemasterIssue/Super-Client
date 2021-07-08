package mod.supergamer5465.sc.ui;

import java.util.concurrent.TimeUnit;

import mod.supergamer5465.sc.Client;
import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.modules.utilities.AutoReconnect;
import mod.supergamer5465.sc.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.GuiConnecting;

public class AutoReconnectButton extends GuiButton {
	public AutoReconnectButton(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, buttonText);

		timer.reset();

		Mod = (AutoReconnect) Main.moduleManager.getModule("AutoReconnect");
		reconnectTimer = Mod.timer.getValue();
	}

	private AutoReconnect Mod;
	private Timer timer = new Timer();
	private long reconnectTimer;

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.drawButton(mc, mouseX, mouseY, partialTicks);

		if (visible) {
			if (Mod.toggled && !timer.getPassedMillis(reconnectTimer))
				this.displayString = "AutoReconnect ("
						+ TimeUnit.MILLISECONDS.toSeconds(Math
								.abs((timer.getPassedTimeMs() + (long) reconnectTimer) - System.currentTimeMillis()))
						+ ")";
			else if (!Mod.toggled)
				this.displayString = "AutoReconnect";

			if (timer.getPassedMillis(reconnectTimer) && Mod.toggled && Client.lastConnectedIP != null
					&& Client.lastConnectedPort != -1) {
				mc.displayGuiScreen(new GuiConnecting(null, mc, Client.lastConnectedIP, Client.lastConnectedPort));
			}
		}
	}

	public void Clicked() {
		Mod.toggle();

		timer.reset();
	}
}
