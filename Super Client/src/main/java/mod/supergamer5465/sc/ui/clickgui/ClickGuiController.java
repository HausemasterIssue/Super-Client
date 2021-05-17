package mod.supergamer5465.sc.ui.clickgui;

import java.io.IOException;
import java.util.ArrayList;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.ui.clickgui.settingeditor.SettingController;
import net.minecraft.client.gui.GuiScreen;

public class ClickGuiController extends GuiScreen {

	public static ClickGuiController INSTANCE = new ClickGuiController();

	static ArrayList<ClickGuiFrame> frames;

	public ClickGuiController() {
		frames = new ArrayList<>();
		int offset = 0;
		for (Category category : Category.values()) {
			frames.add(new ClickGuiFrame(category, 2 + offset, 20));
			offset += 71;
		}
	}

	public static ArrayList<ModuleButton> getButtons() {
		ArrayList<ModuleButton> mb;
		mb = new ArrayList<>();
		for (ClickGuiFrame f : frames) {
			mb.addAll(f.moduleButtons);
		}
		return mb;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (ClickGuiFrame frame : frames) {
			frame.render(mouseX, mouseY);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 0) {
			for (ClickGuiFrame frame : frames) {
				frame.onClick(mouseX, mouseY, mouseButton);
			}
		}

		if (mouseButton == 1) {
			SettingController settingController = new SettingController(mouseX, mouseY);
			mc.displayGuiScreen(settingController);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
