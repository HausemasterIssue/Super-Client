package mod.supergamer5465.sc.ui.clickgui.settingeditor;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

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
		for (SettingButton s : frame.settingButtons) {
			if (s.textBoxSelected) {
				String text = s.text;
				if (keyCode == Keyboard.KEY_DELETE) {
					s.text = text.substring(0, text.length() - 1);
					return;
				} else if (keyCode == Keyboard.KEY_0) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += ")";
					} else {
						text += "0";
					}
				} else if (keyCode == Keyboard.KEY_1) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "!";
					} else {
						text += "1";
					}
				} else if (keyCode == Keyboard.KEY_2) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "@";
					} else {
						text += "2";
					}
				} else if (keyCode == Keyboard.KEY_3) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "#";
					} else {
						text += "3";
					}
				} else if (keyCode == Keyboard.KEY_4) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "$";
					} else {
						text += "4";
					}
				} else if (keyCode == Keyboard.KEY_5) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "%";
					} else {
						text += "5";
					}
				} else if (keyCode == Keyboard.KEY_6) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "^";
					} else {
						text += "6";
					}
				} else if (keyCode == Keyboard.KEY_7) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "&";
					} else {
						text += "7";
					}
				} else if (keyCode == Keyboard.KEY_8) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "*";
					} else {
						text += "8";
					}
				} else if (keyCode == Keyboard.KEY_9) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "(";
					} else {
						text += "9";
					}
				} else if (keyCode == Keyboard.KEY_MINUS) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "_";
					} else {
						text += "-";
					}
				} else if (keyCode == Keyboard.KEY_ADD) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "+";
					} else {
						text += "=";
					}
				} else if (keyCode == Keyboard.KEY_Q) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "Q";
					} else {
						text += "q";
					}
				} else if (keyCode == Keyboard.KEY_W) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "W";
					} else {
						text += "w";
					}
				} else if (keyCode == Keyboard.KEY_E) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "E";
					} else {
						text += "e";
					}
				} else if (keyCode == Keyboard.KEY_R) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "R";
					} else {
						text += "r";
					}
				} else if (keyCode == Keyboard.KEY_T) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "T";
					} else {
						text += "t";
					}
				} else if (keyCode == Keyboard.KEY_Y) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "Y";
					} else {
						text += "y";
					}
				} else if (keyCode == Keyboard.KEY_U) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "U";
					} else {
						text += "u";
					}
				} else if (keyCode == Keyboard.KEY_I) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "I";
					} else {
						text += "i";
					}
				} else if (keyCode == Keyboard.KEY_O) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "O";
					} else {
						text += "o";
					}
				} else if (keyCode == Keyboard.KEY_P) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "P";
					} else {
						text += "p";
					}
				} else if (keyCode == Keyboard.KEY_LBRACKET) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "{";
					} else {
						text += "[";
					}
				} else if (keyCode == Keyboard.KEY_RBRACKET) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "}";
					} else {
						text += "]";
					}
				} else if (keyCode == Keyboard.KEY_BACKSLASH) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "|";
					} else {
						text += "\\";
					}
				} else if (keyCode == Keyboard.KEY_A) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "A";
					} else {
						text += "a";
					}
				} else if (keyCode == Keyboard.KEY_S) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "S";
					} else {
						text += "s";
					}
				} else if (keyCode == Keyboard.KEY_D) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "D";
					} else {
						text += "d";
					}
				} else if (keyCode == Keyboard.KEY_F) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "F";
					} else {
						text += "f";
					}
				} else if (keyCode == Keyboard.KEY_G) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "G";
					} else {
						text += "g";
					}
				} else if (keyCode == Keyboard.KEY_H) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "H";
					} else {
						text += "h";
					}
				} else if (keyCode == Keyboard.KEY_J) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "J";
					} else {
						text += "j";
					}
				} else if (keyCode == Keyboard.KEY_K) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "K";
					} else {
						text += "k";
					}
				} else if (keyCode == Keyboard.KEY_L) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "L";
					} else {
						text += "l";
					}
				} else if (keyCode == Keyboard.KEY_COLON) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += ":";
					} else {
						text += ";";
					}
				} else if (keyCode == Keyboard.KEY_APOSTROPHE) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "\"";
					} else {
						text += "'";
					}
				} else if (keyCode == Keyboard.KEY_Z) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "Z";
					} else {
						text += "z";
					}
				} else if (keyCode == Keyboard.KEY_X) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "X";
					} else {
						text += "x";
					}
				} else if (keyCode == Keyboard.KEY_C) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "C";
					} else {
						text += "c";
					}
				} else if (keyCode == Keyboard.KEY_V) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "V";
					} else {
						text += "v";
					}
				} else if (keyCode == Keyboard.KEY_B) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "B";
					} else {
						text += "b";
					}
				} else if (keyCode == Keyboard.KEY_N) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "N";
					} else {
						text += "n";
					}
				} else if (keyCode == Keyboard.KEY_M) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "M";
					} else {
						text += "m";
					}
				} else if (keyCode == Keyboard.KEY_COMMA) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "<";
					} else {
						text += ",";
					}
				} else if (keyCode == Keyboard.KEY_PERIOD) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += ">";
					} else {
						text += ".";
					}
				} else if (keyCode == Keyboard.KEY_SLASH) {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						text += "?";
					} else {
						text += "/";
					}
				}
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
