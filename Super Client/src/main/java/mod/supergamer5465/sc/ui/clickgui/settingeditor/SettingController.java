package mod.supergamer5465.sc.ui.clickgui.settingeditor;

import java.awt.Color;
import java.io.IOException;

import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.ui.clickgui.ClickGuiController;
import mod.supergamer5465.sc.ui.clickgui.ModuleButton;
import net.minecraft.client.gui.GuiScreen;

public class SettingController extends GuiScreen {

	public SettingFrame frame;

	public Module module;

	public SettingController(int mouseX, int mouseY) {

		for (ModuleButton m : ClickGuiController.getButtons()) {
			if (mouseX >= m.x && mouseX <= m.x + m.width && mouseY >= m.y && mouseY <= m.y + m.height) {
				module = m.module;
			}
		}

		frame = new SettingFrame(module, 20, 20, new Color(255, 255, 255));
	}

	public void refresh(boolean kbClicked) {
		if (kbClicked) {
			frame = new SettingFrame(module, 20, 20, new Color(255, 0, 0));
		} else {
			frame = new SettingFrame(module, 20, 20, new Color(255, 255, 255));
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		frame.render(mouseX, mouseY);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			this.mc.displayGuiScreen(ClickGuiController.INSTANCE);

			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 0) {
			frame.onClick(mouseX, mouseY, mouseButton);
		}
	}
}
