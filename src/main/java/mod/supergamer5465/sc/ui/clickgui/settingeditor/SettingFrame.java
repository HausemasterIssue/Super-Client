package mod.supergamer5465.sc.ui.clickgui.settingeditor;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class SettingFrame {
	int x, y, width, height;

	Minecraft mc = Minecraft.getMinecraft();

	Module module;

	ArrayList<SettingButton> settingButtons;
	KeybindButton kbButton;

	Color kbColor;

	public SettingFrame(Module m, int x, int y, Color kbColor) {
		this.x = x;
		this.y = y;
		this.width = 400;
		this.height = 0;
		this.module = m;
		this.kbColor = kbColor;

		settingButtons = new ArrayList<>();
		int offsetY = 14;

		kbButton = new KeybindButton(module, module.getKey(), x, y + offsetY, this, this.kbColor);
		offsetY += 14;// keybind button space
		if (Main.settingManager.getSettingsByMod(m) != null) {
			for (Setting s : Main.settingManager.getSettingsByMod(m)) {
				settingButtons.add(new SettingButton(module, s, x, y + offsetY, this));
				offsetY += 14;
			}
		}

		for (GuiButton b : m.buttons) {
			b.x = x;
			b.y = y + offsetY;
			b.width = mc.fontRenderer.getStringWidth(b.displayString);
			offsetY += 14;
		}

		this.height = offsetY;
	}

	public void render(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLineWidth(5);

		GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.4f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y + height);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x + width, y);
		GL11.glEnd();

		GL11.glColor3f(0.0f, 200.0f, 255.0f);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y + height);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x + width, y);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glColor3f(1, 1, 1);

		int moduleNameColor = new Color(255, 255, 255).getRGB();
		mc.fontRenderer.drawString(module.getName(), x + 2, y + 2, moduleNameColor);
		kbButton.draw(mouseX, mouseY);
		for (SettingButton s : settingButtons) {
			s.draw(mouseX, mouseY);
		}

		for (GuiButton b : module.buttons) {
			mc.fontRenderer.drawString(b.displayString, b.x + 2, b.y + 2, new Color(255, 255, 255).getRGB());
		}
	}

	public void onClick(int x, int y, int button) {
		for (SettingButton s : settingButtons) {
			s.onClick(x, y, button);
		}
		for (GuiButton g : module.buttons) {
			g.mousePressed(mc, x, y);
			g.mouseReleased(x, y);
		}
		kbButton.onClick(x, y, button);
	}
}
