package mod.supergamer5465.sc.ui.clickgui.settingeditor.selectors;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.setting.settings.BlockSelectorSetting;
import net.minecraft.client.gui.GuiScreen;

public class BlockSelectorGuiController extends GuiScreen {

	public ArrayList<BlockButton> blocks;

	public boolean colorSettings;
	public GuiScreen lastScreen;

	public int scrollOffset = 0;

	public BlockSelectorGuiFrame frame;

	public BlockSelectorSetting setting;

	public BlockSelectorGuiController(GuiScreen lastScreen, boolean colorSettings, BlockSelectorSetting setting) {
		this.lastScreen = lastScreen;
		this.colorSettings = colorSettings;
		this.setting = setting;

		if (setting != null && setting.parent != null)
			frame = new BlockSelectorGuiFrame(20, 20, setting, this);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		scroller();

		if (frame == null) {
			this.onGuiClosed();
			return;
		}

		frame.render(mouseX, mouseY);

	}

	@Override
	public boolean doesGuiPauseGame() {
		super.doesGuiPauseGame();
		return false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {

		if (frame == null)
			return;

		super.keyTyped(typedChar, keyCode);
		if (keyCode == 1) {

			frame.y -= scrollOffset;

			for (BlockButton b : blocks) {
				if (b != null) {
					b.y -= scrollOffset;
					if (b.textFieldRed != null && b.textFieldGreen != null && b.textFieldBlue != null) {
						b.textFieldRed.y -= scrollOffset;
						b.textFieldGreen.y -= scrollOffset;
						b.textFieldBlue.y -= scrollOffset;
						b.textFieldRed.setVisible(false);
						b.textFieldRed.setEnabled(false);
						b.textFieldGreen.setVisible(false);
						b.textFieldGreen.setEnabled(false);
						b.textFieldBlue.setVisible(false);
						b.textFieldBlue.setEnabled(false);
					}
				}
			}

			scrollOffset = 0;

			this.mc.displayGuiScreen(lastScreen);

			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		}
		for (BlockButton b : blocks) {
			if (b != null && b.textFieldRed != null && b.textFieldGreen != null && b.textFieldBlue != null) {
				b.textFieldRed.textboxKeyTyped(typedChar, keyCode);
				b.textFieldGreen.textboxKeyTyped(typedChar, keyCode);
				b.textFieldBlue.textboxKeyTyped(typedChar, keyCode);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (frame == null)
			return;

		if (mouseButton == 0) {
			frame.onClick(mouseX, mouseY, mouseButton);
		}

		for (BlockButton b : blocks) {
			if (b != null)
				if (b.textFieldRed != null && b.textFieldGreen != null && b.textFieldBlue != null) {
					if (mouseX >= b.textFieldRed.x && mouseX <= b.textFieldRed.x + b.textFieldRed.width
							&& mouseY >= b.textFieldRed.y && mouseY <= b.textFieldRed.y + b.textFieldRed.height)
						b.textFieldRed.setFocused(true);
					if (mouseX >= b.textFieldGreen.x && mouseX <= b.textFieldGreen.x + b.textFieldGreen.width
							&& mouseY >= b.textFieldGreen.y && mouseY <= b.textFieldGreen.y + b.textFieldGreen.height)
						b.textFieldGreen.setFocused(true);
					if (mouseX >= b.textFieldBlue.x && mouseX <= b.textFieldBlue.x + b.textFieldBlue.width
							&& mouseY >= b.textFieldBlue.y && mouseY <= b.textFieldBlue.y + b.textFieldBlue.height)
						b.textFieldBlue.setFocused(true);
				}
		}

		Main.config.Save();
	}

	private void scroller() {
		int dWheel = Mouse.getDWheel();
		if (dWheel < 0) {

			for (BlockButton b : blocks) {
				if (b != null) {
					b.y -= 10;
					if (b.textFieldRed != null && b.textFieldGreen != null && b.textFieldBlue != null) {
						b.textFieldRed.y -= 10;
						b.textFieldGreen.y -= 10;
						b.textFieldBlue.y -= 10;
					}
				}
			}

			frame.y -= 10;

			scrollOffset -= 10;

		} else if (dWheel > 0) {

			for (BlockButton b : blocks) {
				if (b != null) {
					b.y += 10;
					if (b.textFieldRed != null && b.textFieldGreen != null && b.textFieldBlue != null) {
						b.textFieldRed.y += 10;
						b.textFieldGreen.y += 10;
						b.textFieldBlue.y += 10;
					}
				}
			}

			frame.y += 10;

			scrollOffset += 10;

		}
	}

	public void refresh() {
		Main.config.Save();
		frame = new BlockSelectorGuiFrame(20, 20, setting, this);
		frame.y += scrollOffset;
	}
}
