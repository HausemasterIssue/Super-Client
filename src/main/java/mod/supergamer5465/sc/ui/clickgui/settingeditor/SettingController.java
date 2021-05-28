package mod.supergamer5465.sc.ui.clickgui.settingeditor;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.ui.clickgui.ClickGuiController;
import mod.supergamer5465.sc.ui.clickgui.ModuleButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class SettingController extends GuiScreen {

	public static Map<GuiTextField, Module> textFields = new HashMap<GuiTextField, Module>();

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

		for (Map.Entry<GuiTextField, Module> entry : textFields.entrySet()) {
			GuiTextField textField = entry.getKey();
			if (entry.getValue() == frame.module)
				textField.drawTextBox();
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		super.doesGuiPauseGame();
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (keyCode == 1) {
			for (GuiTextField t : SettingController.textFields.keySet()) {
				t.setFocused(false);
				t.setTextColor(new Color(255, 255, 255).getRGB());
			}
			this.mc.displayGuiScreen(ClickGuiController.INSTANCE);

			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		}
		for (Map.Entry<GuiTextField, Module> entry : textFields.entrySet()) {
			GuiTextField textField = entry.getKey();
			if (entry.getValue() == frame.module)
				textField.textboxKeyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 0) {
			frame.onClick(mouseX, mouseY, mouseButton);
		}

		for (Map.Entry<GuiTextField, Module> entry : textFields.entrySet()) {
			GuiTextField textField = entry.getKey();
			if (entry.getValue() == frame.module) {
				if (mouseX >= textField.x && mouseX < textField.x + textField.width && mouseY >= textField.y
						&& mouseY < textField.y + textField.height) {
					textField.setFocused(true);
				}
			}
			textField.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		for (Map.Entry<GuiTextField, Module> entry : textFields.entrySet()) {
			GuiTextField textField = entry.getKey();
			if (entry.getValue() == frame.module) {
				if (textField.isFocused())
					textField.updateCursorCounter();
			}
		}
	}
}
