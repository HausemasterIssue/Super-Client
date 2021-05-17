package mod.supergamer5465.sc.ui.clickgui.settingeditor;

import java.awt.Color;

import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;
import net.minecraft.client.Minecraft;

public class SettingButton {
	int x, y, width, height;

	Module module;

	SettingFrame parent;

	Minecraft mc = Minecraft.getMinecraft();

	public SettingButton(Module module, Setting setting, int x, int y, SettingFrame parent) {
		this.module = module;
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.width = parent.width;
		this.height = 14;
	}

	public void draw(int mouseX, int mouseY) {
		if (module.toggled) {
			mc.fontRenderer.drawString(module.getName(), x + 2, y + 2, new Color(255, 150, 50).getRGB());
		} else {
			mc.fontRenderer.drawString(module.getName(), x + 2, y + 2, new Color(180, 240, 255).getRGB());
		}
	}

	public void onClick(int x, int y, int button) {
		if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
			module.toggle();
		}
	}
}
