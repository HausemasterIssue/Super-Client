package mod.supergamer5465.sc.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.ModeSetting;
import mod.supergamer5465.sc.setting.settings.NumberSetting;
import net.minecraft.client.Minecraft;

public class Config {
	private Path modFolder;
	private Path configFile;

	public Config() {
		modFolder = Minecraft.getMinecraft().mcDataDir.toPath().resolve("super");
		configFile = Paths.get(modFolder.toString() + File.separator + "moduleConfig.txt");
		try {
			Files.createDirectories(modFolder);
			if (!Files.exists(configFile))
				Files.createFile(configFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void Save() {
		ArrayList<String> save = new ArrayList<String>();

		for (Module mod : Main.moduleManager.modules) {
			if (mod.getName().equalsIgnoreCase("ClickGUI")) {
				save.add("module:" + mod.getName() + ":false:" + mod.getKey());
			} else {
				save.add("module:" + mod.getName() + ":" + mod.isToggled() + ":" + mod.getKey());
			}
		}

		for (Module mod : Main.moduleManager.modules) {
			for (Setting setting : mod.settings) {
				if (setting instanceof BooleanSetting) {
					BooleanSetting bool = (BooleanSetting) setting;
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + bool.isEnabled());
				}

				if (setting instanceof NumberSetting) {
					NumberSetting numb = (NumberSetting) setting;
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + numb.getValue());
				}

				if (setting instanceof ModeSetting) {
					ModeSetting mode = (ModeSetting) setting;
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + mode.getMode());
				}
			}
		}

		try {
			PrintWriter pw = new PrintWriter(this.configFile.toFile());
			for (String str : save) {
				pw.println(str);
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void Load() {
		ArrayList<String> lines = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.configFile.toFile()));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (String s : lines) {
			String[] args = s.split(":");
			if (s.toLowerCase().startsWith("module:")) {
				Module m = Main.moduleManager.getModule(args[1]);
				if (m != null) {
					m.setKey(Integer.parseInt(args[3]));
					m.setToggled(Boolean.parseBoolean(args[2]));
				}
			} else if (s.toLowerCase().startsWith("setting:")) {
				Module m = Main.moduleManager.getModule(args[1]);
				if (m != null) {
					Setting setting = Main.settingManager.getSettingByName(m, args[2]);
					if (setting != null) {
						Main.settingManager.rSetting(setting);
						if (setting instanceof BooleanSetting) {
							((BooleanSetting) setting).setEnabled(Boolean.parseBoolean(args[3]));
						}
						if (setting instanceof NumberSetting) {
							((NumberSetting) setting).setValue(Double.parseDouble(args[3]));
						}
						if (setting instanceof ModeSetting) {
							((ModeSetting) setting).setMode(args[3]);
						}
					}
				}
			}
		}
	}
}
