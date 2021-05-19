package mod.supergamer5465.sc.ui.clickgui.settingeditor;

import java.awt.Color;

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

public class SettingButton {
	int x, y, width, height;

	Module module;

	SettingFrame parent;

	Minecraft mc = Minecraft.getMinecraft();

	Setting setting;

	public Boolean textBoxSelected = false;

	ModeSetting mSetting;
	IntSetting iSetting;
	FloatSetting fSetting;
	BooleanSetting bSetting;
	ListSetting lSetting;
	StringSetting sSetting;

	String text;
	int color;

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
			text = Integer.toString(iSetting.value);
		} else if (setting.type.equalsIgnoreCase("float")) {
			this.fSetting = (FloatSetting) setting;
			text = Float.toString(fSetting.value);
		} else if (setting.type.equalsIgnoreCase("string")) {
			this.sSetting = (StringSetting) setting;
			text = sSetting.value;
		} else if (setting.type.equalsIgnoreCase("boolean")) {
			this.bSetting = (BooleanSetting) setting;
		} else if (setting.type.equalsIgnoreCase("list")) {
			this.lSetting = (ListSetting) setting;
		}
	}

	public void draw(int mouseX, int mouseY) {
		if (setting.type.equalsIgnoreCase("mode")) {
			mc.fontRenderer.drawString(mSetting.name + ": " + mSetting.getMode(), x + 2, y + 2,
					new Color(255, 255, 255).getRGB());
		} else if (setting.type.equalsIgnoreCase("int")) {
			mc.fontRenderer.drawString(iSetting.name + ": ", x + 2, y + 2, new Color(255, 255, 255).getRGB());
			mc.fontRenderer.drawString(text, x + 2 + mc.fontRenderer.getStringWidth(iSetting.name + ": "), y + 2,
					new Color(255, 255, 255).getRGB());
			if (textBoxSelected) {
				if (StringParser.isInteger(text)) {
					iSetting.value = Integer.valueOf(text);
					color = (new Color(0, 255, 0).getRGB());
					ClickGuiController.INSTANCE.settingController.refresh(false);

				} else {
					color = (new Color(255, 0, 0).getRGB());
					ClickGuiController.INSTANCE.settingController.refresh(false);
				}
			}
		} else if (setting.type.equalsIgnoreCase("float")) {
			mc.fontRenderer.drawString(fSetting.name + ": ", x + 2, y + 2, new Color(255, 255, 255).getRGB());
			mc.fontRenderer.drawString(text, x + 2 + mc.fontRenderer.getStringWidth(fSetting.name + ": "), y + 2,
					new Color(255, 255, 255).getRGB());
			if (textBoxSelected) {
				if (StringParser.isFloat(text)) {
					fSetting.value = Float.valueOf(text);
					color = (new Color(0, 255, 0).getRGB());
					ClickGuiController.INSTANCE.settingController.refresh(false);
				} else {
					color = (new Color(255, 0, 0).getRGB());
					ClickGuiController.INSTANCE.settingController.refresh(false);
				}
			}
		} else if (setting.type.equalsIgnoreCase("string")) {
			mc.fontRenderer.drawString(sSetting.name + ": ", x + 2, y + 2, new Color(255, 255, 255).getRGB());
			mc.fontRenderer.drawString(text, x + 2 + mc.fontRenderer.getStringWidth(sSetting.name + ": "), y + 2,
					new Color(255, 255, 255).getRGB());
			if (textBoxSelected) {
				sSetting.value = text;
				color = (new Color(0, 255, 0).getRGB());
				ClickGuiController.INSTANCE.settingController.refresh(false);
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

	public void onClick(int x, int y, int button) {
		if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
			if (setting.type.equalsIgnoreCase("mode")) {
				mSetting.cycle();
				ClickGuiController.INSTANCE.settingController.refresh(false);
			} else if (setting.type.equalsIgnoreCase("int")) {
				textBoxSelected = true;
			} else if (setting.type.equalsIgnoreCase("float")) {
				textBoxSelected = true;
			} else if (setting.type.equalsIgnoreCase("string")) {
				textBoxSelected = true;
			} else if (setting.type.equalsIgnoreCase("boolean")) {
				if (bSetting.enabled) {
					bSetting.setEnabled(false);
				} else {
					bSetting.setEnabled(true);
				}
			} else if (setting.type.equalsIgnoreCase("list")) {
				// TODO figure this out
			}
		} else {
			textBoxSelected = false;
		}
	}
}
