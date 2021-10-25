package mod.imphack.ui.clickgui.settingeditor;

import mod.imphack.Client;
import mod.imphack.module.Module;
import mod.imphack.ui.clickgui.ClickGuiController;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class KeybindButton {
	int x, y, width, height;

	Module module;

	SettingFrame parent;

	Color color;

	Minecraft mc = Minecraft.getMinecraft();

	public KeybindButton(Module module, int key, int x, int y, SettingFrame parent, Color c) {
		this.module = module;
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.width = parent.width;
		this.height = 14;
		this.color = c;
	}

	public void draw(int mouseX, int mouseY) {
		mc.fontRenderer.drawString("Keybind: " + Keyboard.getKeyName(module.getKey()), x + 2, y + 2, color.getRGB());
	}

	public void onClick(int x, int y, int button) {
		if (x >= this.x && x <= this.x + mc.fontRenderer.getStringWidth("Keybind: ") + 50 && y >= this.y - 5
				&& y <= this.y + this.height - 5) {
			if (!Client.getNextKeyPressForKeybinding) {
				Client.waitForKeybindKeypress(module);
				ClickGuiController.INSTANCE.settingController.refresh(true);
			} else {
				Client.stopWaitForKeybindPress();
				ClickGuiController.INSTANCE.settingController.refresh(false);
			}
		}
	}
}
