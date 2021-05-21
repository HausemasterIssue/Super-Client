package mod.supergamer5465.sc.ui.clickgui.settingeditor;

import java.awt.Color;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.misc.StringParser;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import mod.supergamer5465.sc.setting.settings.ListSetting;
import mod.supergamer5465.sc.setting.settings.ModeSetting;
import mod.supergamer5465.sc.setting.settings.StringSetting;
import mod.supergamer5465.sc.ui.clickgui.ClickGuiController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class SettingButton {
	private GuiTextField textField;

	int x, y, width, height;

	Module module;

	SettingFrame parent;

	Minecraft mc = Minecraft.getMinecraft();

	Setting setting;

	ModeSetting mSetting;
	IntSetting iSetting;
	FloatSetting fSetting;
	BooleanSetting bSetting;
	ListSetting lSetting;
	StringSetting sSetting;

	public SettingButton(Module module, Setting setting, int x, int y, SettingFrame parent) {

		this.module = module;
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.width = parent.width;
		this.height = 14;
		this.setting = setting;

		if (setting.type.equalsIgnoreCase("mode")) {
			this.mSetting = (ModeSetting) setting;
		} else if (setting.type.equalsIgnoreCase("int")) {
			this.iSetting = (IntSetting) setting;
		} else if (setting.type.equalsIgnoreCase("float")) {
			this.fSetting = (FloatSetting) setting;
		} else if (setting.type.equalsIgnoreCase("string")) {
			this.sSetting = (StringSetting) setting;
		} else if (setting.type.equalsIgnoreCase("boolean")) {
			this.bSetting = (BooleanSetting) setting;
		} else if (setting.type.equalsIgnoreCase("list")) {
			this.lSetting = (ListSetting) setting;
		}
	}

	public void draw(int mouseX, int mouseY) {

		for (GuiTextField t : SettingController.textFields) {
			String val = "";
			if (setting.type.equalsIgnoreCase("int")) {
				val = Integer.toString(iSetting.value);
			} else if (setting.type.equalsIgnoreCase("float")) {
				val = Float.toString(fSetting.value);
			} else if (setting.type.equalsIgnoreCase("string")) {
				val = sSetting.value;
			}

			if (t.height == mc.fontRenderer.FONT_HEIGHT + 2
					&& t.width == x + 2 + mc.fontRenderer.getStringWidth(setting.name + ": " + val)
					&& t.x == x + 2 + mc.fontRenderer.getStringWidth(setting.name + ": ") && t.y == y) {
				textField = t;
			}
		}

		if (setting.type.equalsIgnoreCase("mode")) {
			mc.fontRenderer.drawString(mSetting.name + ": " + mSetting.getMode(), x + 2, y + 2,
					new Color(255, 255, 255).getRGB());
		} else if (setting.type.equalsIgnoreCase("int")) {
			mc.fontRenderer.drawString(iSetting.name + ": ", x + 2, y + 2, new Color(255, 255, 255).getRGB());
			int text = mc.fontRenderer.drawString(Integer.toString(iSetting.value),
					x + 2 + mc.fontRenderer.getStringWidth(iSetting.name + ": "), y + 2,
					new Color(255, 255, 255).getRGB());
			if (this.textField == null) {
				this.textField = new GuiTextField(text, mc.fontRenderer,
						x + 2 + mc.fontRenderer.getStringWidth(iSetting.name + ": "), y,
						x + 2 + mc.fontRenderer.getStringWidth(iSetting.name + ": " + Integer.toString(iSetting.value)),
						mc.fontRenderer.FONT_HEIGHT + 2);
				textField.setEnabled(true);
				SettingController.textFields.add(textField);
				textField.setText(Integer.toString(iSetting.value));
			}
			if (textField.isFocused()) {
				if (StringParser.isInteger(textField.getText())) {
					iSetting.value = Integer.valueOf(textField.getText());
					textField.setTextColor(new Color(0, 255, 0).getRGB());

				} else {
					textField.setTextColor(new Color(255, 0, 0).getRGB());
				}
			}
		} else if (setting.type.equalsIgnoreCase("float")) {
			mc.fontRenderer.drawString(fSetting.name + ": ", x + 2, y + 2, new Color(255, 255, 255).getRGB());
			int text = mc.fontRenderer.drawString(Float.toString(fSetting.value),
					x + 2 + mc.fontRenderer.getStringWidth(fSetting.name + ": "), y + 2,
					new Color(255, 255, 255).getRGB());
			if (this.textField == null) {
				this.textField = new GuiTextField(text, mc.fontRenderer,
						x + 2 + mc.fontRenderer.getStringWidth(fSetting.name + ": "), y,
						x + 2 + mc.fontRenderer.getStringWidth(fSetting.name + ": " + Float.toString(fSetting.value)),
						mc.fontRenderer.FONT_HEIGHT + 2);
				textField.setEnabled(true);
				SettingController.textFields.add(textField);
				textField.setText(Float.toString(fSetting.value));
			}
			if (textField.isFocused()) {
				if (StringParser.isFloat(textField.getText())) {
					fSetting.value = Float.valueOf(textField.getText());
					textField.setTextColor(new Color(0, 255, 0).getRGB());
				} else {
					textField.setTextColor(new Color(255, 0, 0).getRGB());
				}
			}
		} else if (setting.type.equalsIgnoreCase("string")) {
			mc.fontRenderer.drawString(sSetting.name + ": ", x + 2, y + 2, new Color(255, 255, 255).getRGB());
			int text = mc.fontRenderer.drawString(sSetting.value,
					x + 2 + mc.fontRenderer.getStringWidth(sSetting.name + ": "), y + 2,
					new Color(255, 255, 255).getRGB());
			if (this.textField == null) {
				this.textField = new GuiTextField(text, mc.fontRenderer,
						x + 2 + mc.fontRenderer.getStringWidth(sSetting.name + ": "), y,
						x + 2 + mc.fontRenderer.getStringWidth(sSetting.name + ": " + sSetting.value),
						mc.fontRenderer.FONT_HEIGHT + 2);
				textField.setEnabled(true);
				SettingController.textFields.add(textField);
				textField.setText(sSetting.value);
			}
			if (textField.isFocused()) {
				sSetting.value = textField.getText();
				textField.setTextColor(new Color(0, 255, 0).getRGB());
			}
		} else if (setting.type.equalsIgnoreCase("boolean")) {
			if (bSetting.enabled) {
				mc.fontRenderer.drawString(bSetting.name, x + 2, y + 2, new Color(255, 150, 50).getRGB());
			} else {
				mc.fontRenderer.drawString(bSetting.name, x + 2, y + 2, new Color(180, 240, 255).getRGB());
			}
		} else if (setting.type.equalsIgnoreCase("list")) {
			// TODO develop
		}
	}

	// mc.fontRenderer.drawString(module.getName(), x + 2, y + 2, new Color(255,
	// 255, 255).getRGB());

	public void onClick(int x, int y, int button) throws NullPointerException {
		if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
			if (setting.type.equalsIgnoreCase("mode")) {
				mSetting.cycle();
				ClickGuiController.INSTANCE.settingController.refresh(false);
			} else if (setting.type.equalsIgnoreCase("int")) {
				// NOOP
			} else if (setting.type.equalsIgnoreCase("float")) {
				// NOOP
			} else if (setting.type.equalsIgnoreCase("string")) {
				// NOOP
			} else if (setting.type.equalsIgnoreCase("boolean")) {
				if (bSetting.enabled) {
					bSetting.setEnabled(false);
				} else {
					bSetting.setEnabled(true);
				}
				ClickGuiController.INSTANCE.settingController.refresh(false);
			} else if (setting.type.equalsIgnoreCase("list")) {
				// TODO figure this out
			}
		} else {
			if (textField != null && textField.isFocused())
				this.textField.setFocused(false);
			if (textField != null) {
				this.textField.setTextColor(new Color(255, 255, 255).getRGB());
				if (setting.type.equalsIgnoreCase("int")) {
					this.textField.setText(Integer.toString(iSetting.value));
				} else if (setting.type.equalsIgnoreCase("float")) {
					this.textField.setText(Float.toString(fSetting.value));
				} else if (setting.type.equalsIgnoreCase("string")) {
					this.textField.setText(sSetting.value);
				}
			}
		}
		Main.config.Save();
		ClickGuiController.INSTANCE.settingController.refresh(false);
	}
}
