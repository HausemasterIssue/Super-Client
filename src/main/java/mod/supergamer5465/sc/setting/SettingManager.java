package mod.supergamer5465.sc.setting;

import java.util.ArrayList;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import net.minecraft.client.gui.GuiScreen;

public class SettingManager extends GuiScreen {
	private ArrayList<Setting> settings;

	public SettingManager() {
		this.settings = new ArrayList<Setting>();
	}

	public void rSetting(Setting in) {
		this.settings.add(in);
	}

	public ArrayList<Setting> getSettings() {
		return this.settings;
	}

	public ArrayList<Setting> getSettingsByMod(Module mod) {
		ArrayList<Setting> out = new ArrayList<Setting>();
		for (Setting s : getSettings()) {
			if (s.parent.equals(mod)) {
				out.add(s);
			}
		}
		if (out.isEmpty()) {
			return null;
		}
		return out;
	}

	public Setting getSettingByName(Module mod, String name) {
		for (Module m : Main.moduleManager.modules) {
			for (Setting set : m.settings) {
				if (set.name.equalsIgnoreCase(name) && set.parent == mod) {
					return set;
				}
			}
		}
		return null;
	}

	public void clearSettings() {
		this.settings.clear();
	}
}
