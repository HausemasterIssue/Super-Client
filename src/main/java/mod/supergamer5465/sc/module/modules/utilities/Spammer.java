package mod.supergamer5465.sc.module.modules.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import mod.supergamer5465.sc.Client;
import mod.supergamer5465.sc.config.Config;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import mod.supergamer5465.sc.util.Timer;

public class Spammer extends Module {

	IntSetting delay = new IntSetting("Delay(s)", this, 300);

	public Spammer() {
		super("Spammer", "Spams in the Chat", Category.UTILITIES);

		addSetting(delay);
	}

	private Path spammerFile = null;

	Timer timer = new Timer();

	@Override
	public void onEnable() {
		spammerFile = Paths.get(Config.modFolder.toString() + File.separator + "spammer.txt");
		try {
			Files.createDirectories(Config.modFolder);
			if (!Files.exists(spammerFile))
				Files.createFile(spammerFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.spammerFile.toFile()));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			this.toggle();
			spammerFile = Paths.get(Config.modFolder.toString() + File.separator + "spammer.txt");
			Client.addChatMessage("couldn't find the spammer file, turning spammer off.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		timer.reset();
	}

	ArrayList<String> lines = new ArrayList<>();
	int index = 0;

	@Override
	public void onUpdate() {
		if (spammerFile == null) {
			spammerFile = Paths.get(Config.modFolder.toString() + File.separator + "spammer.txt");
			return;
		}

		if (lines == null || lines.isEmpty()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(this.spammerFile.toFile()));
				String line = reader.readLine();
				while (line != null) {
					lines.add(line);
					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				this.toggle();
				spammerFile = Paths.get(Config.modFolder.toString() + File.separator + "spammer.txt");
				Client.addChatMessage("couldn't find the spammer file, turning spammer off.");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (timer.getPassedTimeMs() / 1000 >= delay.value) {
				timer.reset();
				if (index == lines.size())
					index = 0;
				mc.player.sendChatMessage(lines.get(index));
				index++;
			}
		}
	}
}
