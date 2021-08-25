package mod.supergamer5465.sc.config;

import java.awt.Color;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Map.Entry;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;
import mod.supergamer5465.sc.setting.settings.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.registry.*;

public class Config {
	public static Path modFolder;
	private Path configFile;

	public Config() {
		modFolder = Minecraft.getMinecraft().gameDir.toPath().resolve("super");
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

				if (setting instanceof IntSetting) {
					IntSetting num = (IntSetting) setting;
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + num.getValue());
				}

				if (setting instanceof FloatSetting) {
					FloatSetting num = (FloatSetting) setting;
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + num.getValue());
				}

				if (setting instanceof StringSetting) {
					StringSetting val = (StringSetting) setting;
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + val.getValue());
				}

				if (setting instanceof ModeSetting) {
					ModeSetting mode = (ModeSetting) setting;
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + mode.getMode());
				}

				if (setting instanceof ColorSetting) {
					ColorSetting col = (ColorSetting) setting;
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + col.red + "," + col.green + ","
							+ col.blue);
				}

				if (setting instanceof BlockSelectorSetting) {
					BlockSelectorSetting block = (BlockSelectorSetting) setting;
					String list = "";
					String colorList = "";
					for (Block b : block.blocks) {
						list += b.getLocalizedName() + "/";
					}
					for (Entry<Block, Color> e : block.colors.entrySet()) {
						colorList += e.getValue().getRGB() + "," + e.getKey() + "/";
					}
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + list + ";" + colorList);
				}

				if (setting instanceof EntitySelectorSetting) {
					EntitySelectorSetting entity = (EntitySelectorSetting) setting;
					String list = "";
					for (EntityEntry e : entity.entities) {
						list += e.getName() + "/";
					}
					save.add("setting:" + mod.getName() + ":" + setting.name + ":" + list);
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

	@SuppressWarnings("deprecation")
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
				try {
					m.setKey(Integer.parseInt(args[3]));
					m.setToggled(Boolean.parseBoolean(args[2]));
				} catch (NullPointerException e) {
					System.out.println("Module in config file does not exist");
					e.printStackTrace();
				}
			} else if (s.toLowerCase().startsWith("setting:")) {
				Module m = Main.moduleManager.getModule(args[1]);
				if (m != null) {
					Setting setting = Main.settingManager.getSettingByName(m, args[2]);
					if (setting != null) {
						Main.settingManager.rSetting(setting);
					}
				}
			}
		}
		ArrayList<String> save = new ArrayList<String>();
		for (String s : lines) {
			save.add(s);
		}
		for (Module mod : Main.moduleManager.getModuleList()) {
			for (Setting setting : mod.settings) {
				if (!Main.settingManager.getSettings().contains(setting)) {

					if (setting instanceof BooleanSetting) {
						BooleanSetting bool = (BooleanSetting) setting;
						save.add("setting:" + mod.getName() + ":" + setting.name + ":" + bool.isEnabled());
					}

					if (setting instanceof IntSetting) {
						IntSetting num = (IntSetting) setting;
						save.add("setting:" + mod.getName() + ":" + setting.name + ":" + num.getValue());
					}

					if (setting instanceof FloatSetting) {
						FloatSetting num = (FloatSetting) setting;
						save.add("setting:" + mod.getName() + ":" + setting.name + ":" + num.getValue());
					}

					if (setting instanceof StringSetting) {
						StringSetting val = (StringSetting) setting;
						save.add("setting:" + mod.getName() + ":" + setting.name + ":" + val.getValue());
					}

					if (setting instanceof ModeSetting) {
						ModeSetting mode = (ModeSetting) setting;
						save.add("setting:" + mod.getName() + ":" + setting.name + ":" + mode.getMode());
					}
					if (setting instanceof ColorSetting) {
						ColorSetting col = (ColorSetting) setting;
						save.add("setting:" + mod.getName() + ":" + setting.name + ":" + col.red + "," + col.green + ","
								+ col.blue);
					}

					if (setting instanceof BlockSelectorSetting) {
						BlockSelectorSetting block = (BlockSelectorSetting) setting;
						String list = "";
						String colorList = "";
						for (Block b : block.blocks) {
							list += b.getLocalizedName() + "/";
						}
						for (Entry<Block, Color> e : block.colors.entrySet()) {
							colorList += e.getValue().getRGB() + "," + e.getKey() + "/";
						}
						save.add("setting:" + mod.getName() + ":" + setting.name + ":" + list + ";" + colorList);
					}

					if (setting instanceof EntitySelectorSetting) {
						EntitySelectorSetting entity = (EntitySelectorSetting) setting;
						String list = "";
						for (EntityEntry e : entity.entities) {
							list += e.getName() + "/";
						}
						save.add("setting:" + mod.getName() + ":" + setting.name + ":" + list);
					}
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

		Main.settingManager.clearSettings();

		lines = new ArrayList<String>();

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

						if (setting instanceof IntSetting) {
							((IntSetting) setting).setValue(Integer.valueOf(args[3]));
						}

						if (setting instanceof FloatSetting) {
							((FloatSetting) setting).setValue(Float.valueOf(args[3]));
						}

						if (setting instanceof StringSetting) {
							((StringSetting) setting).setValue((args[3]));
						}

						if (setting instanceof ModeSetting) {
							((ModeSetting) setting).setMode(args[3]);
						}

						if (setting instanceof ColorSetting) {
							((ColorSetting) setting).red = Integer.valueOf(args[3].split(",")[0]);
							((ColorSetting) setting).green = Integer.valueOf(args[3].split(",")[1]);
							((ColorSetting) setting).blue = Integer.valueOf(args[3].split(",")[2]);
						}

						if (setting instanceof BlockSelectorSetting) {
							if (args.length > 3) {
								try {
									if (args[3].contains(";")) {
										for (String str : args[3].split(";")[0].split("/")) {
											if (!str.isEmpty()) {
												for (Block b : GameRegistry.findRegistry(Block.class)
														.getValuesCollection()) {
													if (str.equalsIgnoreCase(b.getLocalizedName()))
														((BlockSelectorSetting) setting).blocks.add(b);
												}
											}
										}
										for (String str : args[3].split(";")[1].split("/")) {
											if (!str.isEmpty()) {
												for (Block b : GameRegistry.findRegistry(Block.class)
														.getValuesCollection()) {
													if (str.split(",")[1].equalsIgnoreCase(b.getLocalizedName()))
														((BlockSelectorSetting) setting).setColor(b,
																new Color(Integer.valueOf(str)));
												}
											}
										}
									} else {
										for (String str : args[3].split("/")) {
											if (!str.isEmpty()) {
												for (Block b : GameRegistry.findRegistry(Block.class)
														.getValuesCollection()) {
													if (str.equalsIgnoreCase(b.getLocalizedName()))
														((BlockSelectorSetting) setting).blocks.add(b);
												}
											}
										}
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									// System.out.println("config issue");
								}
							}
						}

						if (setting instanceof EntitySelectorSetting) {
							if (args.length >= 4) {
								for (String str : args[3].split("/")) {
									if (!str.isEmpty()) {
										for (EntityEntry e : GameRegistry.findRegistry(EntityEntry.class).getValues()) {
											if (e.getName() == str)
												((EntitySelectorSetting) setting).entities.add(e);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
