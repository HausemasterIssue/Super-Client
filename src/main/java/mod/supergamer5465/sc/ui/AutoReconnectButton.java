package mod.supergamer5465.sc.ui;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.modules.utilities.Reconnect;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import mod.supergamer5465.sc.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.GuiConnecting;

public class AutoReconnectButton extends GuiButton {
	public AutoReconnectButton(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, buttonText);

		timer = new Timer();
		timer.reset();

		Mod = (Reconnect) Main.moduleManager.getModule("Reconnect");
		reconnectTimer = (long) ((IntSetting) Main.settingManager
				.getSettingByName(Main.moduleManager.getModule("Reconnect"), "Timer")).value;
	}

	private Reconnect Mod;
	private Timer timer = new Timer();
	private long reconnectTimer;

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.drawButton(mc, mouseX, mouseY, partialTicks);

		if (visible && ((Reconnect) Main.moduleManager.getModule("Reconnect")).serverData != null) {
			reconnectTimer = (long) ((IntSetting) Main.settingManager
					.getSettingByName(Main.moduleManager.getModule("Reconnect"), "Timer")).value;
			if (Mod.toggled && timer.getPassedTimeMs() < reconnectTimer)
				this.displayString = "AutoReconnect " + Long.toString((long) reconnectTimer - timer.getPassedTimeMs());
			else if (!Mod.toggled)
				this.displayString = "Enable AutoReconnect";
			else {
				mc.displayGuiScreen(new GuiConnecting(null, mc,
						((Reconnect) Main.moduleManager.getModule("Reconnect")).serverData));
			}

			if (timer.getPassedTimeMs() + 100 >= reconnectTimer && Mod.toggled
					&& ((Reconnect) Main.moduleManager.getModule("Reconnect")).serverData != null) {
				mc.displayGuiScreen(new GuiConnecting(null, mc,
						((Reconnect) Main.moduleManager.getModule("Reconnect")).serverData));
			}
		}
	}

	public void Clicked() {
		Mod.toggle();

		timer.reset();
	}
}
