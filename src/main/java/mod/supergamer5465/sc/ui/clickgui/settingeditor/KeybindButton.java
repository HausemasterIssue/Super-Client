package mod.supergamer5465.sc.ui.clickgui.settingeditor;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import mod.supergamer5465.sc.Client;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.ui.clickgui.ClickGuiController;
import net.minecraft.client.Minecraft;

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
			Client.waitForKeybindKeypress(module);
			ClickGuiController.INSTANCE.settingController.refresh(true);
		}
	}
}
